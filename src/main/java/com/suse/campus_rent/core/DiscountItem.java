package com.suse.campus_rent.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

// 优惠项
@Data
@AllArgsConstructor
public class DiscountItem {
    private String key;        // 如 "会员折扣"
    private String description;
    private BigDecimal amount;
}
