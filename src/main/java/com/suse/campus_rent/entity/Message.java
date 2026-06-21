package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("message")
public class Message {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属对话ID
     */
    private Long conversationId;

    /**
     * 发送者用户ID
     */
    private Long userId;

    /**
     * 消息内容（文本或JSON格式附件信息）
     */
    private String text;

    /**
     * 消息类型：1-文本，2-图片，3-文件，4-语音
     */
    private Integer type;

    /**
     * 发送时间
     */
    private LocalDateTime time;

    private Integer state;
}