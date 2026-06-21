package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;
    private String iphone;          // 数据库字段为 iphone
    private String email;
    private String nickname;
    private String realName;
    private String avatar;
    private String drivingLicense;
    private Integer totalRentals;
    private Integer totalMileage;
    private Integer points;
    private String password;
    private Integer state;
    private String role;

    private LocalDateTime lastLoginTime;      // 最后登录时间
    private BigDecimal totalSpent;             // 累计消费
    private BigDecimal balance;                 // 账户余额
    private LocalDate studentExpireTime;
    private String gender;                      // 性别
    private String address;                     // 联系地址
    private String idCard;                      // 身份证号


    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}