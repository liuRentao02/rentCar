package com.suse.campus_rent.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

// 费用项
@Data
@AllArgsConstructor
public class FeeItem {
    private String key;        // 如 "基础租金"
    private String description;
    private BigDecimal amount;
}
