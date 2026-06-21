package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("order_penalty")
public class OrderPenalty {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private String reason;
    private BigDecimal amount;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}