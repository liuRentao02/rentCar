package com.suse.campus_rent.vo.admin;

import lombok.Data;

@Data
public class NoticeStatisticsVO {
    private Integer total;      // 对应 SQL 中的 total
    private Integer published;
    private Integer draft;
    private Integer archived;
}