package com.suse.campus_rent.dto.app;

import lombok.Data;

/**
 * RegisterUser
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/2 15:58
 */
@Data
public class RegisterUser {
    private String username;
    private String phone;
    private String email;
    private String password;
    private String nickname;
}
