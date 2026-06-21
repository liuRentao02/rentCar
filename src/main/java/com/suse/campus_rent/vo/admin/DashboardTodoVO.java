package com.suse.campus_rent.vo.admin;

import lombok.Data;

@Data
public class DashboardTodoVO {
    private String type;      // 类型：实名认证/押金退还/车辆报修/投诉处理
    private String content;   // 内容描述
    private String time;      // 时间（相对时间或格式化时间）
    private String tagType;   // ElementPlus标签类型：primary/success/warning/danger
}