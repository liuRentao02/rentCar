package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("notice_attachment")
public class NoticeAttachment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long noticeId;
    private String fileName;
    private String fileUrl;
    private Long fileSize;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}