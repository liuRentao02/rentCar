package com.suse.campus_rent.service.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.dto.admin.AdminConfirmReturnDTO;
import com.suse.campus_rent.dto.admin.OrderQueryDTO;
import com.suse.campus_rent.dto.admin.OrderStatusUpdateDTO;
import com.suse.campus_rent.vo.admin.OrderDetailVO;
import com.suse.campus_rent.vo.admin.OrderVO;

import java.util.Map;

public interface OrderService {
    /**
     * 获取订单统计（卡片数据）
     */
    Map<String, Object> getStatistics();

    /**
     * 分页查询订单列表
     */
    IPage<OrderVO> listOrders(OrderQueryDTO queryDTO);

    /**
     * 获取订单详情
     */
    OrderDetailVO getOrderDetail(String orderNo);

    /**
     * 确认取车
     */
    void confirmPickup(OrderStatusUpdateDTO dto, String operator);

    /**
     * 确认还车
     */
    void confirmReturn(AdminConfirmReturnDTO dto, String operator);

    /**
     * 取消订单
     */
    void cancelOrder(OrderStatusUpdateDTO dto, String operator);

    /**
     * 标记异常
     */
    void markAbnormal(OrderStatusUpdateDTO dto, String operator);
}