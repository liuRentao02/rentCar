package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单增值服务关联表实体类
 */
@Data
@TableName("order_service")
public class OrderServices {
    @TableId(type = IdType.AUTO)
    private Long id; // 关联ID

    private Long orderId; // 订单ID

    private Long serviceId; // 服务ID

    private String serviceNameSnapshot; // 服务名称快照

    private BigDecimal priceSnapshot; // 服务单价快照

    private Integer quantity; // 数量（通常为租车天数）

    private BigDecimal totalPrice; // 该项服务总价

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}