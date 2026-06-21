package com.suse.campus_rent.dto.admin;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class UserCreateDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;               // 用户名（与nickname可相同）

    private String nickname;                // 昵称

    private String realName;   // 新增

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;                   // 手机号

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;                    // 邮箱

    @NotBlank(message = "角色不能为空")
    private String role;                      // 角色

    private String idCard;                    // 身份证号

    private String drivingLicense;            // 驾驶证号

    private Integer state;                     // 状态：0-禁用，1-正常

    private String gender;                     // 性别：male/female

    private String address;                    // 联系地址

    private Integer points;

    private String remark;                     // 备注（忽略，不存储）
}