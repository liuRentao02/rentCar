package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserProfileVO {
    private Long id;
    private String username;
    private String nickname;
    private String realName;
    private String email;
    private String phone;           // 对应 iphone
    private String avatar;
    private String role;             // 角色名称
    private LocalDateTime registerTime;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
}