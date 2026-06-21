package com.suse.campus_rent.vo.admin;

import lombok.Data;

@Data
public class DashboardWarningVO {
    private String level;     // 级别：high/medium/low
    private String title;     // 标题
    private String desc;      // 描述
}