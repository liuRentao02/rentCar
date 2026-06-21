package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContactMessageVO {
    private Long id;
    private Long userId;
    private String userName;      // 用户昵称或真实姓名
    private String userPhone;     // 用户手机号
    private String subject;
    private String message;
    private LocalDateTime createTime;
}