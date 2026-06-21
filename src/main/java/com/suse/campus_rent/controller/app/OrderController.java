package com.suse.campus_rent.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.dto.app.ExtendOrderDTO;
import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.app.OrderEstimateDTO;
import com.suse.campus_rent.dto.app.OrderSubmitDTO;
import com.suse.campus_rent.service.app.service.OrderService;
import com.suse.campus_rent.vo.app.OrderDetailVO;
import com.suse.campus_rent.vo.app.OrderListItemVO;
import com.suse.campus_rent.core.PriceResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @OperLog(title = "提交订单", category = "ORDER", level = "INFO")
    @PostMapping
    public Result<?> submitOrder(@RequestBody @Valid OrderSubmitDTO dto,
                                 @RequestParam("userId") Long userId) {
        Long l = orderService.submitOrder(dto, userId);
        return Result.success(l);
    }

    @OperLog(title = "取消订单", category = "ORDER", level = "WARN")
    @PutMapping("/{id}/cancel")
    public Result<?> cancelOrder(@PathVariable Long id, @RequestParam("userId") Long userId) {
        orderService.cancelOrder(id, userId);
        return Result.success("订单已取消");
    }

    // 查询订单详情 - 不加日志
    @GetMapping("/{id}")
    public Result<OrderDetailVO> getOrderDetail(@PathVariable Long id,
                                                @RequestParam("userId") Long userId) {
        OrderDetailVO detail = orderService.getOrderDetail(id, userId);
        return Result.success(detail);
    }

    // 查询我的订单列表 - 不加日志
    @GetMapping("/my")
    public Result<IPage<OrderListItemVO>> getMyOrders(@RequestParam Long userId,
                                                      @RequestParam(defaultValue = "1") Integer page,
                                                      @RequestParam(defaultValue = "10") Integer size,
                                                      @RequestParam(required = false) String status) {
        IPage<OrderListItemVO> pageResult = orderService.getMyOrders(userId, page, size, status);
        return Result.success(pageResult);
    }

    @OperLog(title = "支付订单", category = "ORDER", level = "INFO")
    @PutMapping("/{id}/pay")
    public Result<?> payOrder(@PathVariable Long id, @RequestParam("userId") Long userId, @RequestParam(value = "paymentMethod", required = false) String paymentMethod) {
        orderService.payOrder(id, userId, paymentMethod);
        return Result.success("支付成功");
    }

    @OperLog(title = "申请还车", category = "ORDER", level = "INFO")
    @PutMapping("/{id}/applyReturn")
    public Result<?> applyReturn(@PathVariable Long id, @RequestParam("userId") Long userId) {
        orderService.applyReturn(id, userId);
        return Result.success("还车申请已提交");
    }

    // 费用预估 - 仅查询，不加日志
    @PostMapping("/estimate")
    public Result<PriceResult> estimatePrice(@RequestBody @Valid OrderEstimateDTO dto,
                                             @RequestParam("userId") Long userId) {
        PriceResult priceResult = orderService.estimatePrice(dto, userId);
        return Result.success(priceResult);
    }

    @GetMapping("/{id}/extend/estimate")
    public Result<BigDecimal> estimateExtend(@PathVariable Long id, @RequestParam Integer extraDays) {
        BigDecimal cost = orderService.estimateExtendCost(id, extraDays);
        return Result.success(cost);
    }

    @PostMapping("/{id}/extend/pay")
    public Result<?> payExtend(@PathVariable Long id,
                               @RequestParam Long userId,
                               @RequestParam String paymentMethod,
                               @RequestParam Integer extraDays) {
        orderService.payExtend(id, userId, extraDays, paymentMethod);
        return Result.success("延长租期成功");
    }
}