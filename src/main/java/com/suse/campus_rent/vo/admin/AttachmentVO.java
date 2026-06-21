package com.suse.campus_rent.vo.admin;

import lombok.Data;

@Data
public class AttachmentVO {
    private Long id;
    private String name;   // 原文件名
    private String url;    // 文件访问URL
    private Long size;
}