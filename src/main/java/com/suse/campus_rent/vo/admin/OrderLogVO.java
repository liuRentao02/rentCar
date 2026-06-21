package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderLogVO {
    private String content;
    private LocalDateTime time;
}