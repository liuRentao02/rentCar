package com.suse.campus_rent.dto.admin;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class UserUpdateDTO {
    @NotNull(message = "用户ID不能为空")
    private Long id;

    private String realName;

    private String username;

    private String nickname;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String role;

    private String idCard;

    private String drivingLicense;

    private Integer state;

    private String gender;

    private String address;

    private String remark;

    private Integer points;
}