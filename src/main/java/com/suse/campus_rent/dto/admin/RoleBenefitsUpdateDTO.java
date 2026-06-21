package com.suse.campus_rent.dto.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoleBenefitsUpdateDTO {

    private Long id;
    private String role;                     // 通常不允许修改角色标识，但保留字段
    private BigDecimal depositRate;
    private BigDecimal rentDiscount;
    private Integer freeExtensionCount;
    private BigDecimal overdueFeeRate;
    private Integer pointsThreshold;
    private String description;
}