package com.suse.campus_rent.dto.admin;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private Long id;
    private String oldPassword;
    private String newPassword;
}