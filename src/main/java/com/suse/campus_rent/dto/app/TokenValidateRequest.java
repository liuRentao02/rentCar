package com.suse.campus_rent.dto.app;

import lombok.Data;

@Data
public class TokenValidateRequest {
    private String token;      // 前端传入的 JWT
    private String username;    // 用户名
    private String role;        // 角色（如 "admin" 或 "user"）
}