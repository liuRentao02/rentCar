package com.suse.campus_rent.service.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.common.enums.CarStatusEnum;
import com.suse.campus_rent.common.OrderStatus;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.core.DiscountItem;
import com.suse.campus_rent.core.FeeItem;
import com.suse.campus_rent.dto.app.OrderEstimateDTO;
import com.suse.campus_rent.dto.app.OrderSubmitDTO;
import com.suse.campus_rent.core.PriceKeys;
import com.suse.campus_rent.entity.*;
import com.suse.campus_rent.mapper.*;
import com.suse.campus_rent.core.PriceResult;
import com.suse.campus_rent.service.app.service.OrderService;
import com.suse.campus_rent.vo.app.OrderDetailVO;
import com.suse.campus_rent.vo.app.OrderListItemVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final CarInfoMapper carInfoMapper;   // 统一用新表
    private final CarModelsMapper carModelsMapper;// 统一用新表
    private final UserMapper userMapper;
    private final ServiceMapper serviceMapper;
    private final OrderServiceMapper orderServiceMapper;
    private final RoleBenefitsMapper roleBenefitsMapper;
    private final OrderLogMapper orderLogMapper;
    private final ReviewMapper reviewMapper;

    private String generateOrderNo() {
        return "ORD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    private void setLog(Long orderId, String who, String action, String description) {
        OrderLog log = new OrderLog();
        log.setOrderId(orderId);
        log.setOperator(who);
        log.setAction(action);
        log.setContent(description);
        orderLogMapper.insert(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitOrder(OrderSubmitDTO dto, Long userId) {
        checkUserConcurrentOrders(userId);

        CarInfo car = carInfoMapper.selectById(dto.getCarId());
        if (car == null || !"available".equals(car.getStatus())) {
            throw new BusinessException("车辆不可租");
        }

        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException("用户不存在");
        RoleBenefits roleBenefits = roleBenefitsMapper.selectOne(
                new LambdaQueryWrapper<RoleBenefits>().eq(RoleBenefits::getRole, user.getRole()));
        if (roleBenefits == null) throw new BusinessException("角色权益不存在");

        // 统一计算价格
        OrderEstimateDTO estimateDTO = new OrderEstimateDTO();
        estimateDTO.setServiceIds(dto.getServiceIds());
        estimateDTO.setCarId(dto.getCarId());
        estimateDTO.setTotalDays(dto.getTotalDays());
        PriceResult priceResult = estimatePrice(estimateDTO, userId);

        // 提取明细
        BigDecimal baseRent = BigDecimal.ZERO, baseDeposit = BigDecimal.ZERO, servicesTotal = BigDecimal.ZERO;
        for (FeeItem item : priceResult.getFeeItems()) {
            if (PriceKeys.BASE_RENT.getKey().equals(item.getKey())) {
                baseRent = item.getAmount();
            } else if (PriceKeys.DEPOSIT.getKey().equals(item.getKey())) {
                baseDeposit = item.getAmount();
            } else {
                servicesTotal = servicesTotal.add(item.getAmount());
            }
        }
        BigDecimal rentDiscount = BigDecimal.ZERO, depositDiscount = BigDecimal.ZERO;
        if (priceResult.getDiscountItems() != null) {
            for (DiscountItem discount : priceResult.getDiscountItems()) {
                if (PriceKeys.BASE_RENT_DISCOUNT.getKey().equals(discount.getKey())) {
                    rentDiscount = discount.getAmount();
                } else if (PriceKeys.DEPOSIT_DISCOUNT.getKey().equals(discount.getKey())) {
                    depositDiscount = discount.getAmount();
                }
            }
        }

        BigDecimal discountedRent = baseRent.subtract(rentDiscount);
        BigDecimal discountedDeposit = baseDeposit.subtract(depositDiscount);

        // 获取服务列表
        List<Services> services = Collections.emptyList();
        if (dto.getServiceIds() != null && !dto.getServiceIds().isEmpty()) {
            services = serviceMapper.selectByIds(dto.getServiceIds());
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setCarId(dto.getCarId());
        order.setRoleAtRental(user.getRole());
        order.setDailyRentSnapshot(car.getDailyRent());
        order.setRentAmount(discountedRent);               // 折扣后租金
        order.setDepositSnapshot(discountedDeposit);       // 折扣后押金
        order.setContactName(user.getRealName() != null ? user.getRealName() : user.getNickname());
        order.setContactPhone(user.getIphone());
        order.setContactEmail(user.getEmail());
        order.setPickupLocation(dto.getPickupLocation());
        order.setReturnLocation(dto.getReturnLocation());
        order.setRentStartTime(dto.getRentStartTime());
        order.setRentEndTime(dto.getRentEndTime());
        order.setTotalDays(dto.getTotalDays());
        order.setTotalAmount(priceResult.getFinalTotal()); // 包含押金的最终应付
        order.setOverdueFee(BigDecimal.ZERO);
        order.setPenaltyAmount(BigDecimal.ZERO);
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        orderMapper.insert(order);

        if (!services.isEmpty()) {
            for (Services s : services) {
                OrderServices os = new OrderServices();
                os.setOrderId(order.getId());
                os.setServiceId(s.getId());
                os.setServiceNameSnapshot(s.getName());
                os.setPriceSnapshot(s.getPrice());
                os.setQuantity(dto.getTotalDays());
                os.setTotalPrice(s.getPrice().multiply(BigDecimal.valueOf(dto.getTotalDays())));
                orderServiceMapper.insert(os);
            }
        }

        setLog(order.getId(), user.getUsername(), "新增订单", "提交订单");
        return order.getId();
    }

    @Override
    public OrderDetailVO getOrderDetail(Long orderId, Long userId) {
        // 1. 订单校验
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException("订单不存在");
        if (!order.getUserId().equals(userId)) throw new BusinessException("无权查看此订单");

        OrderDetailVO vo = new OrderDetailVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setCreateTime(order.getCreateTime());
        vo.setStatusText(order.getStatus());

        User user = userMapper.selectById(userId);
        int MAX_EXTEND_TIMES = roleBenefitsMapper.selectOne(new LambdaQueryWrapper<RoleBenefits>()
                .eq(RoleBenefits::getRole, user.getRole())
                .select(RoleBenefits::getFreeExtensionCount)).getFreeExtensionCount();

        vo.setExtension(MAX_EXTEND_TIMES);

        // 2. 订单操作权限
        vo.setCanCancel(OrderStatus.PENDING_PAYMENT.equals(order.getStatus()));
        vo.setCanPay(OrderStatus.PENDING_PAYMENT.equals(order.getStatus()));
        vo.setCanApplyPickup(OrderStatus.PAID.equals(order.getStatus()));
        vo.setCanApplyReturn(OrderStatus.RENTING.equals(order.getStatus()));

        // 3. 车辆信息
        CarInfo car = carInfoMapper.selectById(order.getCarId());
        if (car != null) {
            vo.setDailyRent(car.getDailyRent());
            vo.setCarImage(getFirstImage(car.getImageUrls()));

            CarModels model = carModelsMapper.selectById(car.getModelId());
            if (model != null) {
                vo.setCarName(model.getModelName());
                List<String> details = getStringList(model, car);
                vo.setCarDetails(details);
            }
        }

        // 4. 行程与联系人
        vo.setPickupLocation(order.getPickupLocation());
        vo.setReturnLocation(order.getReturnLocation());
        vo.setRentStartTime(order.getRentStartTime());
        vo.setRentEndTime(order.getRentEndTime());
        vo.setTotalDays(order.getTotalDays());
        vo.setContactName(order.getContactName());
        vo.setContactPhone(order.getContactPhone());
        vo.setContactEmail(order.getContactEmail());
        vo.setRole(order.getRoleAtRental());

        // 5. 是否已评价
        vo.setReviewed(reviewMapper.selectCount(new LambdaQueryWrapper<Review>()
                .eq(Review::getOrderId, order.getId()).eq(Review::getUserId, userId)) > 0);

        // 6. 增值服务列表
        List<OrderServices> orderServices = orderServiceMapper.selectList(
                new LambdaQueryWrapper<OrderServices>().eq(OrderServices::getOrderId, orderId));
        vo.setServices(orderServices);

        // 7. 统一计算价格（使用与下单相同的 estimatePrice）
        OrderEstimateDTO estimateDTO = new OrderEstimateDTO();
        estimateDTO.setCarId(order.getCarId());
        estimateDTO.setTotalDays(order.getTotalDays());
        estimateDTO.setServiceIds(orderServices.stream().map(OrderServices::getServiceId).collect(Collectors.toList()));
        PriceResult priceResult = estimatePrice(estimateDTO, userId);
        vo.setPriceResult(priceResult);  // 前端可直接渲染费用明细

        // 8. 从 PriceResult 中提取各项金额，填充 VO（确保与 priceResult 一致）
        BigDecimal baseRent = BigDecimal.ZERO;
        BigDecimal servicesTotal = BigDecimal.ZERO;
        if (priceResult.getFeeItems() != null) {
            for (FeeItem item : priceResult.getFeeItems()) {
                if (PriceKeys.BASE_RENT.getKey().equals(item.getKey())) {
                    baseRent = item.getAmount();
                } else {
                    servicesTotal = servicesTotal.add(item.getAmount());
                }
            }
        }
        BigDecimal rentDiscount = BigDecimal.ZERO;
        BigDecimal depositDiscount = BigDecimal.ZERO;
        if (priceResult.getDiscountItems() != null) {
            for (DiscountItem discount : priceResult.getDiscountItems()) {
                if (PriceKeys.BASE_RENT_DISCOUNT.getKey().equals(discount.getKey())) {
                    rentDiscount = discount.getAmount();
                } else if (PriceKeys.DEPOSIT_DISCOUNT.getKey().equals(discount.getKey())) {
                    depositDiscount = discount.getAmount();
                }
            }
        }

        // 填充车辆费用、服务费、折扣等（这些字段如果 VO 中保留，就按此计算）
        vo.setCarSubtotal(baseRent);                             // 基础租金
        BigDecimal discountedRent = baseRent.subtract(rentDiscount);
        vo.setSubtotal(discountedRent.add(servicesTotal));       // 折扣后租金+服务费（不含押金）
        vo.setDiscount(rentDiscount.add(depositDiscount));       // 总折扣金额
        vo.setTotalAmount(priceResult.getFinalTotal());          // 最终应付（含押金）

        // 9. 押金信息（直接使用订单快照，不再重复计算）
        vo.setDepositOriginal(order.getDepositSnapshot());       // 折扣后押金（已存快照）
        vo.setDepositPayable(order.getDepositSnapshot());
        vo.setDepositDiscountRate(BigDecimal.ONE);               // 实际折扣已在快照中体现
        vo.setDepositStatus(getDepositStatus(order));

        // 10. 违约金
        vo.setPenaltyAmount(order.getPenaltyAmount());
        vo.setPenaltyReason(order.getInspectionDescription());

        return vo;
    }

    private static List<String> getStringList(CarModels model, CarInfo car) {
        List<String> details = new ArrayList<>();
        details.add("👤 " + (model.getSeatCount() != null ? model.getSeatCount() + "座" : "5座"));
        details.add("⚙️ " + (model.getGearboxType() != null ? model.getGearboxType() : "自动"));
        if ("纯电动".equals(model.getEnergyType())) {
            details.add("⚡ " + (car.getCurrentMileage() != null && car.getCurrentMileage() > 0 ? car.getCurrentMileage() + "km续航" : "600km续航"));
        } else {
            details.add("⛽ " + (model.getDisplacement() != null ? model.getDisplacement() : "未知排量"));
        }
        return details;
    }

    private String getDepositStatus(Order order) {
        if (OrderStatus.CANCELLED.equals(order.getStatus())) return "已取消";
        if (OrderStatus.PAID.equals(order.getStatus()) || OrderStatus.PENDING_PICKUP.equals(order.getStatus())
                || OrderStatus.RENTING.equals(order.getStatus()) || OrderStatus.PENDING_RETURN.equals(order.getStatus())) {
            return "已支付";
        } else if (OrderStatus.COMPLETED.equals(order.getStatus())) {
            return "已退还";
        }
        return "待支付";
    }

    @Override
    public IPage<OrderListItemVO> getMyOrders(Long userId, Integer page, Integer size, String status) {
        Page<Order> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .eq(status != null && !status.isEmpty(), Order::getStatus, status)
                .orderByDesc(Order::getCreateTime);

        Page<Order> orderPage = orderMapper.selectPage(pageParam, wrapper);

        List<OrderListItemVO> voList = orderPage.getRecords().stream().map(order -> {
            OrderListItemVO vo = new OrderListItemVO();
            vo.setId(order.getId());
            vo.setOrderNo(order.getOrderNo());
            vo.setRentStartTime(order.getRentStartTime());
            vo.setRentEndTime(order.getRentEndTime());
            vo.setTotalDays(order.getTotalDays());
            vo.setTotalAmount(order.getTotalAmount());
            vo.setStatusText(order.getStatus());

            CarInfo car = carInfoMapper.selectById(order.getCarId());
            if (car != null) {
                CarModels carModels = carModelsMapper.selectById(car.getModelId());
                vo.setCarName(carModels.getBrandName());
                vo.setCarImage(getFirstImage(car.getImageUrls()));
            } else {
                vo.setCarName("未知车辆");
                vo.setCarImage("https://images.unsplash.com/photo-1549317661-bd32c8ce0db2?w=400");
            }
            return vo;
        }).collect(Collectors.toList());

        Page<OrderListItemVO> resultPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        resultPage.setRecords(voList);
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long orderId, Long userId, String paymentMethod) {
        Order order = orderMapper.selectById(orderId);

        if (order == null || !order.getUserId().equals(userId)) throw new BusinessException("订单不存在或无权限");
        if (!OrderStatus.PENDING_PAYMENT.equals(order.getStatus())) throw new BusinessException("当前状态不可支付");

        CarInfo car = carInfoMapper.selectById(order.getCarId());
        if (car == null) throw new BusinessException("关联车辆不存在");
        if (!"available".equals(car.getStatus())) throw new BusinessException("车辆当前不可租（已被预订或维修中）");

        checkUserConcurrentOrders(userId);

        if ("balance".equals(paymentMethod)) {
            User user = userMapper.selectById(userId);
            if (user.getBalance().compareTo(order.getTotalAmount()) < 0)
                throw new BusinessException("余额不足，请选择其他支付方式");
            user.setBalance(user.getBalance().subtract(order.getTotalAmount()));
            userMapper.updateById(user);
        }

        // 更新车辆状态 (新表字符串状态)
        car.setStatus(CarStatusEnum.RENTED.getCode());
        carInfoMapper.updateById(car);

        order.setStatus(OrderStatus.PENDING_PICKUP);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentTime(LocalDateTime.now());
        orderMapper.updateById(order);

        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setTotalSpent(user.getTotalSpent().add(order.getTotalAmount()));
            user.setPoints(user.getPoints() + order.getTotalAmount().intValue());
            userMapper.updateById(user);
            setLog(orderId, user.getUsername(), "已支付", "用户支付订单费用");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyReturn(Long orderId, Long userId) {
        updateOrderStatus(orderId, userId, OrderStatus.RENTING, OrderStatus.PENDING_RETURN, "当前状态不可申请还车", "已申请还车", "用户申请还车");
    }

    @Override
    public void cancelOrder(Long id, Long userId) {
        updateOrderStatus(id, userId, null, OrderStatus.CANCELLED, "订单不存在或无权限", "已取消", "用户取消订单");
    }

    private void updateOrderStatus(Long orderId, Long userId, String expectedStatus, String targetStatus, String errorMsg, String logAction, String logDesc) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) throw new BusinessException(errorMsg);
        if (expectedStatus != null && !expectedStatus.equals(order.getStatus())) throw new BusinessException(errorMsg);

        order.setStatus(targetStatus);
        orderMapper.updateById(order);

        User user = userMapper.selectById(userId);
        setLog(orderId, user.getUsername(), logAction, logDesc);
    }

    private void checkUserConcurrentOrders(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException("用户不存在");

        long activeCount = orderMapper.selectCount(new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .in(Order::getStatus, Arrays.asList(OrderStatus.PAID, OrderStatus.PENDING_PICKUP, OrderStatus.RENTING, OrderStatus.PENDING_RETURN)));

        if (activeCount >= 1) {
            throw new BusinessException("您当前已有 " + activeCount + " 个进行中的订单，已达上限，请完成后再下单");
        }
    }

    @Override
    public BigDecimal estimateExtendCost(Long orderId, Integer extraDays) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException("订单不存在");
        // 获取用户租车时的角色
        RoleBenefits role = roleBenefitsMapper.selectOne(
                new LambdaQueryWrapper<RoleBenefits>().eq(RoleBenefits::getRole, order.getRoleAtRental())
        );
        if (role == null) throw new BusinessException("角色权益配置错误");
        BigDecimal dailyRent = order.getDailyRentSnapshot();
        // 额外费用 = 日租金 * 天数 * 租金折扣率
        BigDecimal cost = dailyRent.multiply(BigDecimal.valueOf(extraDays))
                .multiply(role.getRentDiscount());
        return cost.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payExtend(Long orderId, Long userId, Integer extraDays, String paymentMethod) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在或无权限");
        }
        if (!OrderStatus.RENTING.equals(order.getStatus())) {
            throw new BusinessException("当前订单状态不可延长租期");
        }

        User select = userMapper.selectById(userId);
        int MAX_EXTEND_TIMES = roleBenefitsMapper.selectOne(new LambdaQueryWrapper<RoleBenefits>()
                .eq(RoleBenefits::getRole, select.getRole())
                .select(RoleBenefits::getFreeExtensionCount)).getFreeExtensionCount();
        long extendCount = getExtendCount(orderId);
        if (extendCount >= MAX_EXTEND_TIMES) {
            throw new BusinessException("延长次数已达上限（" + MAX_EXTEND_TIMES + "次），无法再次延长");
        }

        // 计算额外费用
        BigDecimal extraCost = estimateExtendCost(orderId, extraDays);

        // 车辆冲突检查（与原逻辑相同）
        CarInfo car = carInfoMapper.selectById(order.getCarId());
        if (car == null) throw new BusinessException("车辆信息不存在");
        LocalDateTime newEndTime = order.getRentEndTime().plusDays(extraDays);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getCarId, order.getCarId())
                .ne(Order::getId, order.getId())
                .in(Order::getStatus, Arrays.asList(OrderStatus.PAID, OrderStatus.PENDING_PICKUP, OrderStatus.RENTING, OrderStatus.PENDING_RETURN))
                .and(w -> w.between(Order::getRentStartTime, order.getRentStartTime(), newEndTime)
                        .or().between(Order::getRentEndTime, order.getRentStartTime(), newEndTime));
        Long conflictCount = orderMapper.selectCount(wrapper);
        if (conflictCount > 0) {
            throw new BusinessException("车辆在延长的时间段内已被其他订单占用，无法延长");
        }

        // 根据支付方式扣款
        String payMethodName = "balance".equals(paymentMethod) ? "余额支付" :
                ("wechat".equals(paymentMethod) ? "微信支付" :
                        ("alipay".equals(paymentMethod) ? "支付宝" : paymentMethod));
        if ("balance".equals(paymentMethod)) {
            User user = userMapper.selectById(userId);
            if (user.getBalance().compareTo(extraCost) < 0) {
                throw new BusinessException("余额不足，请选择其他支付方式");
            }
            user.setBalance(user.getBalance().subtract(extraCost));
            userMapper.updateById(user);
        }

        // 更新订单
        order.setRentEndTime(newEndTime);
        order.setTotalDays(order.getTotalDays() + extraDays);
        order.setTotalAmount(order.getTotalAmount().add(extraCost));
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);

        // 记录日志
        User user = userMapper.selectById(userId);
        setLog(orderId, user.getUsername(), "EXTEND_PAY",
                String.format("用户支付延长租期%d天费用，金额：%s，方式：%s", extraDays, extraCost, payMethodName));
    }

    //获取订单预计价格
    @Override
    public PriceResult estimatePrice(OrderEstimateDTO dto, Long userId) {
        //空值检测
        if (dto.getCarId() == 0L || userId == 0L || dto.getTotalDays() < 1) {
            throw new BusinessException("参数错误");
        }
        Long carSize = carInfoMapper.selectCount(new LambdaQueryWrapper<CarInfo>().eq(CarInfo::getCarId, dto.getCarId()));
        Long userSize = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getId, userId));


        if (carSize == 0 || userSize == 0) {
            throw new BusinessException("参数有误");
        }

        CarInfo car = carInfoMapper.selectById(dto.getCarId());
        User user = userMapper.selectById(userId);
        RoleBenefits role = roleBenefitsMapper.selectOne(new LambdaQueryWrapper<RoleBenefits>().eq(RoleBenefits::getRole, user.getRole()));

        PriceResult result = new PriceResult();
        //1.租金
        BigDecimal rent = car.getDailyRent().multiply(BigDecimal.valueOf(dto.getTotalDays()));
        result.addFeeItem(PriceKeys.BASE_RENT.getKey(), PriceKeys.BASE_RENT.getDisplayName(), rent);

        //2.押金
        BigDecimal depositAmount = car.getDepositAmount();
        result.addFeeItem(PriceKeys.DEPOSIT.getKey(), PriceKeys.DEPOSIT.getDisplayName(), depositAmount);

        if (role != null) {
            //3.租金折扣
            BigDecimal rentDiscount = rent.multiply(BigDecimal.ONE.subtract(role.getRentDiscount()));
            result.addDiscountItem(PriceKeys.BASE_RENT_DISCOUNT.getKey(), PriceKeys.BASE_RENT_DISCOUNT.getDisplayName(), rentDiscount);

            //4.押金优惠
            BigDecimal depositDiscount = depositAmount.multiply(BigDecimal.ONE.subtract(role.getDepositRate()));
            result.addDiscountItem(PriceKeys.DEPOSIT_DISCOUNT.getKey(), PriceKeys.DEPOSIT_DISCOUNT.getDisplayName(), depositDiscount);
        }

        //5.额外服务费用（无折扣）
        if (!dto.getServiceIds().isEmpty()) {
            List<Services> services = serviceMapper.selectByIds(dto.getServiceIds());
            for (Services service : services) {
                //获取每个服务的价格 * 天数
                BigDecimal serviceItemTotal = service.getPrice().multiply(BigDecimal.valueOf(dto.getTotalDays()));
                result.addFeeItem(service.getType(), service.getDescription(), serviceItemTotal);
            }
        }
        return result;
    }


    // ================= 工具类方法 =================
    private String getFirstImage(String imageUrls) {
        if (imageUrls != null && !imageUrls.isEmpty()) {
            return imageUrls.split(",")[0].trim();
        }
        return "https://images.unsplash.com/photo-1549317661-bd32c8ce0db2?w=400";
    }

    private long getExtendCount(Long orderId) {
        LambdaQueryWrapper<OrderLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderLog::getOrderId, orderId)
                .eq(OrderLog::getAction, "EXTEND_PAY");
        return orderLogMapper.selectCount(wrapper);
    }
}