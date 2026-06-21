package com.suse.campus_rent.dto.admin;

import lombok.Data;

@Data
public class ProfileUpdateDTO {
    private Long id;
    private String nickname;
    private String realName;
    private String idCard;
    private String phone;
    private String email;
    private String gender;
    private String address;
}