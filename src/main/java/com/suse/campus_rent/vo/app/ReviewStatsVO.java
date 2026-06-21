package com.suse.campus_rent.vo.app;

import lombok.Data;

@Data
public class ReviewStatsVO {
    private Double avgRating;      // 综合评分
    private Integer totalReviews;  // 用户评价数
    private String positiveRate;   // 好评率（如“98%”）
    private String totalOrders;    // 成功订单数（如“15,000+”）
}