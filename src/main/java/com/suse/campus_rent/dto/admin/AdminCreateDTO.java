package com.suse.campus_rent.dto.admin;

import lombok.Data;

/**
 * AdminCreateDTO
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/31 10:45
 */
@Data
public class AdminCreateDTO {
    private Long userId;
    private Long auditUserId;
    private String studentId;
    private Integer status;
    private String school;
    private String realName;
    private String imageUrls;
}
