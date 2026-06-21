package com.suse.campus_rent.dto.admin;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Data
public class RoleBenefitsCreateDTO {
    @NotBlank(message = "角色标识不能为空")
    private String role;

    @NotNull(message = "押金比例不能为空")
    private BigDecimal depositRate;

    @NotNull(message = "租金折扣不能为空")
    private BigDecimal rentDiscount;

    @NotNull(message = "免费延长次数不能为空")
    private Integer freeExtensionCount;

    @NotNull(message = "超时费率系数不能为空")
    private BigDecimal overdueFeeRate;

    @NotNull(message = "积分不能为空")
    private Integer pointsThreshold;

    private String description;
}