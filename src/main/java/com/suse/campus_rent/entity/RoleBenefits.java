package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户角色权益配置表实体类
 */
@Data
@TableName("role_benefits")
public class RoleBenefits {
    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID

    private String role; // 角色标识，如ordinary, student, admin

    private BigDecimal depositRate; // 押金比例，1.0=100%押金，0=免押金

    private BigDecimal rentDiscount; // 租金折扣，1.0=无折扣，0.8=八折

    private Integer freeExtensionCount; // 每订单免费延长次数

    private BigDecimal overdueFeeRate; // 超时费率系数，如1.5表示1.5倍

    private String description; // 角色权益描述

    private Integer pointsThreshold; // 新增字段
}