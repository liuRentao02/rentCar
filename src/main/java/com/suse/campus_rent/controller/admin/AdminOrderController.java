package com.suse.campus_rent.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.admin.AdminConfirmReturnDTO;
import com.suse.campus_rent.dto.admin.OrderQueryDTO;
import com.suse.campus_rent.dto.admin.OrderStatusUpdateDTO;
import com.suse.campus_rent.service.admin.service.OrderService;
import com.suse.campus_rent.vo.admin.OrderDetailVO;
import com.suse.campus_rent.vo.admin.OrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminOrderController {

    private final OrderService orderService;

    /**
     * 获取订单统计
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        return Result.success(orderService.getStatistics());
    }

    /**
     * 分页查询订单列表
     */
    @GetMapping
    public Result<IPage<OrderVO>> listOrders(OrderQueryDTO queryDTO) {
        return Result.success(orderService.listOrders(queryDTO));
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{orderNo}")
    public Result<OrderDetailVO> getOrderDetail(@PathVariable String orderNo) {
        return Result.success(orderService.getOrderDetail(orderNo));
    }

    /**
     * 确认取车
     */
    @OperLog(title = "确认取车", category = "ORDER", level = "INFO")
    @PutMapping("/confirmPickup")
    public Result<?> confirmPickup(@RequestBody OrderStatusUpdateDTO dto) {
        String operator = getCurrentUsername();
        orderService.confirmPickup(dto, operator);
        return Result.success("确认取车成功");
    }

    /**
     * 确认还车
     */
    @OperLog(title = "确认还车", category = "ORDER", level = "INFO")
    @PutMapping("/confirmReturn")
    public Result<?> confirmReturn(@RequestBody AdminConfirmReturnDTO dto) {
        String operator = getCurrentUsername();
        orderService.confirmReturn(dto, operator);
        return Result.success("确认还车成功");
    }

    /**
     * 取消订单
     */
    @OperLog(title = "取消订单", category = "ORDER", level = "WARN")
    @PutMapping("/cancel")
    public Result<?> cancelOrder(@RequestBody OrderStatusUpdateDTO dto) {
        String operator = getCurrentUsername();
        orderService.cancelOrder(dto, operator);
        return Result.success("取消订单成功");
    }

    /**
     * 标记异常
     */
    @OperLog(title = "标记订单异常", category = "ORDER", level = "WARN")
    @PutMapping("/abnormal")
    public Result<?> markAbnormal(@RequestBody OrderStatusUpdateDTO dto) {
        String operator = getCurrentUsername();
        orderService.markAbnormal(dto, operator);
        return Result.success("标记异常成功");
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }
}
