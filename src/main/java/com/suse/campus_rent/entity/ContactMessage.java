package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("contact_message")
public class ContactMessage {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String subject;
    private String message;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}