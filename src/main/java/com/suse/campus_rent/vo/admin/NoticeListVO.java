package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeListVO {
    private Long id;
    private String title;
    private String content;          // 列表显示时可能截断，但前端自行处理
    private String type;
    private String status;
    private String priority;
    private String publisher;
    private LocalDateTime publishTime;
    private LocalDateTime scheduledTime;
    private Integer viewCount;
    private LocalDateTime expireTime;
    // 列表不需要附件列表
}