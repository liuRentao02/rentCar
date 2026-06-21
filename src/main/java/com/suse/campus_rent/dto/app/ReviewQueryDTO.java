package com.suse.campus_rent.dto.app;

import lombok.Data;

@Data
public class ReviewQueryDTO {
    private Integer page = 1;
    private Integer size = 6;
    private Integer rating;        // 评分筛选（如5,4,3）
    private String vehicle;        // 车型筛选（suv/sedan/mpv/electric）
    private String sortBy;         // latest/highest/helpful
}