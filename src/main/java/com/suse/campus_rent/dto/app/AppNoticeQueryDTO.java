package com.suse.campus_rent.dto.app;

import lombok.Data;

@Data
public class AppNoticeQueryDTO {
    private Integer page = 1;
    private Integer size = 10;
    private Long id; // 用户id
    private String keyword;      // 标题/内容搜索
    private String type;         // system, activity, maintenance, other
}