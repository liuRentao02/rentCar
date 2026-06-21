package com.suse.campus_rent.dto.app;

import lombok.Data;

@Data
public class CertificationApplyDTO {
    private Long userId;
    private String realName;
    private String studentId;
    private String school;
    private String idCard;          // 可选
    private String imageUrls;        // 图片URL，多个用逗号分隔
}