package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_operation_log")
public class UserOperationLog {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private String username;
    private String action;
    private String content;
    private String result;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}