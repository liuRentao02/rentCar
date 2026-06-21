package com.suse.campus_rent.vo.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String username;          // 登录账号
    private String nickname;           // 昵称
    private String realName;            // 真实姓名
    private String email;
    private String phone;               // 对应iphone
    private String avatarColor;         // 头像颜色（前端生成，后端可不返回，或返回固定值）
    private String role;
    private Integer rentalCount;        // 租车次数（对应totalRentals）
    private Integer status;              // 状态：active/inactive/pending（映射state）
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerTime; // 注册时间（createTime）
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLogin;     // 最后登录时间（lastLoginTime）
    private String idCard;
    private String licenseNo;            // 驾驶证号（drivingLicense）
    private String gender;
    private String address;
    private BigDecimal totalSpent;       // 累计消费
    private BigDecimal balance;          // 账户余额
    private Integer points;      // 新增：积分
}