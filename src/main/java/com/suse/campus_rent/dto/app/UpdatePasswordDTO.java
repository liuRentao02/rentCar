package com.suse.campus_rent.dto.app;

import lombok.Data;

/**
 * UpdatePasswordDTO
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/5/23 13:38
 */
@Data
public class UpdatePasswordDTO {
    private Long userId;
    private String oldPassword;
    private String newPassword;
}
