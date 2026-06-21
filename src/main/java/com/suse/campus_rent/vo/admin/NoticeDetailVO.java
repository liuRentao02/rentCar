package com.suse.campus_rent.vo.admin;

import com.suse.campus_rent.entity.NoticeAttachment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoticeDetailVO {
    private Long id;
    private String title;
    private String content;
    private String type;
    private String status;
    private String priority;
    private String publisher;
    private LocalDateTime publishTime;
    private LocalDateTime createTime;
    private Integer viewCount;
    private List<NoticeAttachment> attachments;   // 附件列表
}