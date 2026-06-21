package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统动态配置表实体类
 */
@Data
@TableName("system_configuration")
public class SystemConfiguration {
    @TableId(type = IdType.AUTO)
    private Long id; // 配置ID，主键

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String type = String.valueOf(0); //类别

    private String configKey; // 配置键，唯一

    private String configValue; // 配置值

    private String description; // 配置描述

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime effectiveTime; // 生效时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime; // 过期时间

    @TableField(fill = FieldFill.INSERT) // 插入时自动填充
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时自动填充
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime; // 最后更新时间
}