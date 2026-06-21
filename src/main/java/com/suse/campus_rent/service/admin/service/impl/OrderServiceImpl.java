package com.suse.campus_rent.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.dto.admin.AdminConfirmReturnDTO;
import com.suse.campus_rent.dto.admin.PenaltyItemDTO;
import com.suse.campus_rent.common.enums.CarStatusEnum;
import com.suse.campus_rent.common.OrderStatus;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.dto.admin.OrderQueryDTO;
import com.suse.campus_rent.dto.admin.OrderStatusUpdateDTO;
import com.suse.campus_rent.core.PriceResult;
import com.suse.campus_rent.entity.*;
import com.suse.campus_rent.mapper.*;
import com.suse.campus_rent.service.admin.service.OrderService;
import com.suse.campus_rent.vo.admin.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("adminOrderService")
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderLogMapper orderLogMapper;
    private final UserMapper userMapper;
    private final CarInfoMapper carInfoMapper;
    private final CarModelsMapper carModelMapper;
    private final OrderServiceMapper orderServiceMapper;
    private final OrderPenaltyMapper orderPenaltyMapper;
    private final RoleBenefitsMapper roleBenefitsMapper;
    private static final Set<String> PAID_STATUSES = Set.of(
            OrderStatus.PAID, OrderStatus.PENDING_PICKUP, OrderStatus.RENTING, OrderStatus.PENDING_RETURN
    );

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> result = orderMapper.selectStatistics();

        if (result == null) {
            result = new HashMap<>();
        }

        result.put("all", result.remove("allCount"));
        return result;
    }

    @Override
    public IPage<OrderVO> listOrders(OrderQueryDTO queryDTO) {
        Page<OrderVO> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        return orderMapper.selectOrderList(page, queryDTO);
    }

    @Override
    public OrderDetailVO getOrderDetail(String orderNo) {
        // 查询订单
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        Order order = orderMapper.selectOne(wrapper);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 查询用户（获取身份证、驾驶证）
        User user = userMapper.selectById(order.getUserId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 查询车辆及车型
        CarInfo car = carInfoMapper.selectById(order.getCarId());
        if (car == null) {
            throw new BusinessException("车辆不存在");
        }
        CarModels carModel = carModelMapper.selectById(car.getModelId());

        // 查询操作日志
        List<OrderLog> logs = orderLogMapper.selectLogsByOrderId(order.getId());
        List<OrderLogVO> logVOs = logs.stream()
                .map(log -> {
                    OrderLogVO vo = new OrderLogVO();
                    vo.setContent(log.getContent());
                    vo.setTime(log.getCreateTime());
                    return vo;
                })
                .collect(Collectors.toList());

        // 计算车辆租金小计
        BigDecimal carSubtotal = order.getDailyRentSnapshot().multiply(BigDecimal.valueOf(order.getTotalDays()));

        // 查询增值服务
        List<ServiceVO> serviceVOs = new ArrayList<>();
        QueryWrapper<OrderServices> wp = new QueryWrapper<>();
        wp.eq("order_id", order.getId());
        List<OrderServices> orderServices = orderServiceMapper.selectList(wp);
        BigDecimal servicesSubtotal = BigDecimal.ZERO;
        for (OrderServices os : orderServices) {
            ServiceVO svo = new ServiceVO();
            svo.setId(os.getServiceId());
            svo.setName(os.getServiceNameSnapshot());
            svo.setPrice(os.getPriceSnapshot());
            serviceVOs.add(svo);
            servicesSubtotal = servicesSubtotal.add(os.getTotalPrice());
        }

        // 计算折扣
        BigDecimal subtotal = carSubtotal.add(servicesSubtotal);
        BigDecimal discount = subtotal.subtract(order.getTotalAmount());
        if (discount.compareTo(BigDecimal.ZERO) < 0) discount = BigDecimal.ZERO;

        // 判断押金状态
        String depositStatus = "未收取";
        if (OrderStatus.PAID.equals(order.getStatus()) ||
                OrderStatus.PENDING_PICKUP.equals(order.getStatus()) ||
                OrderStatus.RENTING.equals(order.getStatus()) ||
                OrderStatus.PENDING_RETURN.equals(order.getStatus())) {
            depositStatus = "已收取";
        } else if (OrderStatus.CANCELLED.equals(order.getStatus())) {
            depositStatus = "已退还";
        }

        // ========== 新增：计算优惠明细 ==========
        PriceResult priceResult = new PriceResult();
        BigDecimal rentOriginal = order.getDailyRentSnapshot().multiply(BigDecimal.valueOf(order.getTotalDays()));
        BigDecimal depositOriginal = car.getDepositAmount();

        // 获取用户租车时的角色权益
        RoleBenefits roleBenefits = roleBenefitsMapper.selectOne(
                new LambdaQueryWrapper<RoleBenefits>().eq(RoleBenefits::getRole, order.getRoleAtRental())
        );
        if (roleBenefits != null) {
            // 租金优惠
            BigDecimal rentDiscountAmount = rentOriginal.multiply(BigDecimal.ONE.subtract(roleBenefits.getRentDiscount()));
            if (rentDiscountAmount.compareTo(BigDecimal.ZERO) > 0) {
                priceResult.addDiscountItem("BASE_RENT_DISCOUNT", "租金优惠", rentDiscountAmount);
            }
            // 押金优惠
            BigDecimal depositDiscountAmount = depositOriginal.multiply(BigDecimal.ONE.subtract(roleBenefits.getDepositRate()));
            if (depositDiscountAmount.compareTo(BigDecimal.ZERO) > 0) {
                priceResult.addDiscountItem("DEPOSIT_DISCOUNT", "押金优惠", depositDiscountAmount);
            }
        }


        // ========== 新增：查询罚金明细 ==========
        List<OrderPenalty> penalties = orderPenaltyMapper.selectList(
                new LambdaQueryWrapper<OrderPenalty>().eq(OrderPenalty::getOrderId, order.getId())
        );
        List<PenaltyItemVO> penaltyVOs = penalties.stream()
                .map(p -> {
                    PenaltyItemVO item = new PenaltyItemVO();
                    item.setReason(p.getReason());
                    item.setAmount(p.getAmount());
                    return item;
                })
                .collect(Collectors.toList());


        // 组装 VO
        OrderDetailVO vo = new OrderDetailVO();
        vo.setOrderNo(order.getOrderNo());
        vo.setStatus(order.getStatus());
        vo.setCreateTime(order.getCreateTime());
        vo.setPaymentMethod(order.getPaymentMethod() != null ? order.getPaymentMethod() : "微信支付");

        vo.setUserName(order.getContactName());
        vo.setUserPhone(order.getContactPhone());
        vo.setIdCard(user.getIdCard());
        vo.setLicenseNo(user.getDrivingLicense());
        vo.setUserRole(order.getRoleAtRental());                     // 用户角色

        if (carModel != null) {
            vo.setVehicleType(carModel.getModelName());
            vo.setVehicleName(carModel.getBrandName() + " " + carModel.getSeriesName());
        }
        vo.setPlate(car.getPlateNumber());
        vo.setColor(car.getVehicleColor());

        vo.setPickupTime(order.getRentStartTime());
        vo.setReturnTime(order.getRentEndTime());
        vo.setDays(order.getTotalDays());
        vo.setPickupLocation(order.getPickupLocation());
        vo.setReturnLocation(order.getReturnLocation());

        vo.setAmount(order.getTotalAmount());

        vo.setPenaltyAmount(order.getPenaltyAmount());
        vo.setPenaltyReason(order.getInspectionDescription());

        // 新字段赋值
        vo.setDeposit(order.getDepositSnapshot());
        vo.setDepositStatus(depositStatus);
        vo.setServices(serviceVOs);
        vo.setDiscount(discount);
        vo.setCarSubtotal(carSubtotal);
        vo.setServicesSubtotal(servicesSubtotal);

        vo.setLogs(logVOs);
        vo.setPenalties(penaltyVOs);
        vo.setPriceResult(priceResult);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmPickup(OrderStatusUpdateDTO dto, String operator) {
        Order order = getOrderByNo(dto.getOrderNo());
        if (!OrderStatus.PAID.equals(order.getStatus()) && !OrderStatus.PENDING_PICKUP.equals(order.getStatus())) {
            throw new BusinessException("订单状态不是待取车审核，不能确认取车");
        }

        order.setStatus(OrderStatus.RENTING);
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);

        CarInfo car = carInfoMapper.selectById(order.getCarId());
        if (car != null) {
            car.setStatus(String.valueOf(CarStatusEnum.RENTED.getText()));
            carInfoMapper.updateById(car);
        }

        saveLog(order.getId(), operator, "PICKUP", "管理员确认取车，订单进入租赁中");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmReturn(AdminConfirmReturnDTO dto, String operator) {
        String orderNo = dto.getOrderNo();
        Order order = getOrderByNo(orderNo);
        log.info("确认还车 - 订单: {}", order.getOrderNo());

        if (!OrderStatus.PENDING_RETURN.equals(order.getStatus())) {
            throw new BusinessException("订单状态不是待还车审核，不能确认还车");
        }

        List<PenaltyItemDTO> penaltyItems = dto.getPenalties();
        BigDecimal totalPenalty = BigDecimal.ZERO;
        BigDecimal overdueFee = BigDecimal.ZERO;

        // 遍历罚金列表，累加总罚金，并提取超时费
        if (penaltyItems != null && !penaltyItems.isEmpty()) {
            for (PenaltyItemDTO item : penaltyItems) {
                totalPenalty = totalPenalty.add(item.getAmount());
                // 识别超时费项：reason 包含“超时”或精确匹配“超时费”
                if (item.getReason() != null && (item.getReason().contains("超时") || "超时费".equals(item.getReason()))) {
                    overdueFee = overdueFee.add(item.getAmount()); // 如果有多个超时费项则累加，通常只有一个
                }
                // 保存罚金明细
                OrderPenalty penaltyRecord = new OrderPenalty();
                penaltyRecord.setOrderId(order.getId());
                penaltyRecord.setReason(item.getReason());
                penaltyRecord.setAmount(item.getAmount());
                orderPenaltyMapper.insert(penaltyRecord);
            }
        }

        // ========== 押金处理 ==========
        BigDecimal depositSnapshot = order.getDepositSnapshot() == null ? BigDecimal.ZERO : order.getDepositSnapshot();
        BigDecimal depositRefund = depositSnapshot.subtract(totalPenalty);
        if (depositRefund.compareTo(BigDecimal.ZERO) < 0) {
            depositRefund = BigDecimal.ZERO;
            log.warn("罚金总额 {} 超过押金 {}，押金将全额扣除", totalPenalty, depositSnapshot);
        }

        // 退还剩余押金到用户余额
        if (depositRefund.compareTo(BigDecimal.ZERO) > 0) {
            User user = userMapper.selectById(order.getUserId());
            user.setBalance(user.getBalance().add(depositRefund));
            userMapper.updateById(user);
        }

        // ========== 更新订单 ==========
        order.setActualReturnTime(LocalDateTime.now());
        order.setPenaltyAmount(totalPenalty);
        order.setOverdueFee(overdueFee);           // 单独记录超时费
        order.setInspectionDescription(dto.getDescription());
        order.setStatus(OrderStatus.COMPLETED);
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);

        // 更新车辆状态
        CarInfo car = carInfoMapper.selectById(order.getCarId());
        if (car != null) {
            car.setStatus(CarStatusEnum.AVAILABLE.getCode());
            carInfoMapper.updateById(car);
        }

        // 增加用户租车次数
        User user = userMapper.selectById(order.getUserId());
        if (user != null) {
            user.setTotalRentals(user.getTotalRentals() + 1);
            userMapper.updateById(user);
        }

        // 记录操作日志
        saveLog(order.getId(), operator, "RETURN",
                String.format("管理员确认还车，罚金总额：%s，超时费：%s，押金退还：%s，实际还车时间：%s",
                        totalPenalty, overdueFee, depositRefund, order.getActualReturnTime()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(OrderStatusUpdateDTO dto, String operator) {
        Order order = getOrderByNo(dto.getOrderNo());
        if (OrderStatus.COMPLETED.equals(order.getStatus()) || OrderStatus.CANCELLED.equals(order.getStatus())) {
            throw new BusinessException("订单已完成或已取消，不能取消");
        }

        boolean paid = PAID_STATUSES.contains(order.getStatus());

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);

        // 释放车辆（如果被占用）
        if (OrderStatus.RENTING.equals(order.getStatus()) || OrderStatus.PENDING_PICKUP.equals(order.getStatus())
                || OrderStatus.PENDING_RETURN.equals(order.getStatus())) {
            CarInfo car = carInfoMapper.selectById(order.getCarId());
            if (car != null) {
                car.setStatus(String.valueOf(CarStatusEnum.AVAILABLE.getDbValue()));
                carInfoMapper.updateById(car);
            }
        }

        saveLog(order.getId(), operator, "CANCEL", "管理员取消订单" + (paid ? "，已退款" : ""));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAbnormal(OrderStatusUpdateDTO dto, String operator) {
        Order order = getOrderByNo(dto.getOrderNo());
        if (OrderStatus.ABNORMAL.equals(order.getStatus())) {
            throw new BusinessException("订单已是异常状态");
        }
        order.setStatus(OrderStatus.ABNORMAL);
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        saveLog(order.getId(), operator, "ABNORMAL", "管理员标记为异常");
    }

    // ========== 私有方法 ==========

    private Order getOrderByNo(String orderNo) {
        Order order = orderMapper.selectOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderNo));
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        return order;
    }

    private void saveLog(Long orderId, String operator, String action, String content) {
        OrderLog log = new OrderLog();
        log.setOrderId(orderId);
        log.setOperator(operator);
        log.setAction(action);
        log.setContent(content);
        orderLogMapper.insert(log);
    }
}