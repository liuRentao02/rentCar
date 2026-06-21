package com.suse.campus_rent.dto.app;

import lombok.Data;

/**
 * ForgetUser
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/28 22:38
 */
@Data
public class ForgetUser {
    private Long id;
    private String account;
    private String password;
}
