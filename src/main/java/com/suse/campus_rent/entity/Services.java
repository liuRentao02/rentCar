package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 增值服务表实体类
 */
@Data
@TableName("service")
public class Services {
    @TableId(type = IdType.AUTO)
    private Long id; // 服务ID

    private String name; // 服务名称

    private String description; // 服务描述

    private BigDecimal price; // 每日单价

    private String type; // 服务类型：insurance/child_seat/wifi等

    private Integer status; // 状态：0-下架，1-上架

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}