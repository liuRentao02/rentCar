package com.suse.campus_rent.dto.app;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

@Data
public class UserUpdateDTO {
    private Long id;
    private String realName;
    private String username;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String iphone;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String drivingLicense;
    private String avatar;
}