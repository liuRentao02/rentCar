package com.suse.campus_rent.vo.app;

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
    private String priority;
    private LocalDateTime publishTime;
    private Integer viewCount;
    private List<NoticeAttachment> noticeAttachments;
}