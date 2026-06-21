package com.suse.campus_rent.service.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.dto.app.OrderEstimateDTO;
import com.suse.campus_rent.dto.app.OrderSubmitDTO;
import com.suse.campus_rent.vo.app.OrderDetailVO;
import com.suse.campus_rent.vo.app.OrderListItemVO;
import com.suse.campus_rent.core.PriceResult;

import java.math.BigDecimal;

/**
 * OrderServices
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/2 00:30
 */
public interface OrderService {
    Long submitOrder(OrderSubmitDTO dto, Long userId);

    OrderDetailVO getOrderDetail(Long id, Long userId);

    IPage<OrderListItemVO> getMyOrders(Long userId, Integer page, Integer size, String status);

    void payOrder(Long orderId, Long userId, String paymentMethod);

    void applyReturn(Long orderId, Long userId);

    void cancelOrder(Long id, Long userId);

    PriceResult estimatePrice(OrderEstimateDTO dto, Long userId);

    BigDecimal estimateExtendCost(Long orderId, Integer extraDays);

    void payExtend(Long id, Long userId, Integer extraDays, String paymentMethod);
}
