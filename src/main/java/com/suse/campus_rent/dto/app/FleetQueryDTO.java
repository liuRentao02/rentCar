package com.suse.campus_rent.dto.app;

import lombok.Data;

@Data
public class FleetQueryDTO {
    // 分页参数
    private Integer page = 1;
    private Integer size = 6;

    // 搜索关键字
    private String keyword;
}