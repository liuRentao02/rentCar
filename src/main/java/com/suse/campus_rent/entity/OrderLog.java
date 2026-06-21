package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("order_log")
public class OrderLog {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;
    private String operator;
    private String action;
    private String content;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}