package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_notice_read")
public class UserNoticeRead {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long noticeId;
    private LocalDateTime readTime;
    private LocalDateTime createTime;
    private LocalDateTime deleteTime;
}