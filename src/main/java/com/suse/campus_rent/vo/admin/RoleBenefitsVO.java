package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoleBenefitsVO {
    private Long id;
    private String role;
    private BigDecimal depositRate;
    private BigDecimal rentDiscount;
    private Integer freeExtensionCount;
    private BigDecimal overdueFeeRate;
    private String description;
    private Integer pointsThreshold;  // 新增
}