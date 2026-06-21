package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * SystemLog
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/4/2 10:54
 */
@Data
@TableName("system_log")
public class SystemLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String level;
    private String category;
    private String content;
    private String detail;

    @TableField("user_id")
    private Long userId;
    private String username;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
