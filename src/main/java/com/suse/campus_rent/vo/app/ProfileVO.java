package com.suse.campus_rent.vo.app;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProfileVO {
    // 用户卡片
    private String avatar;
    private String username;           // 显示名称
    private String role;
    // 统计卡片
    private Integer totalRentals;
    private String totalMileage;    // 带千位分隔符的里程
    private Integer points;
    private BigDecimal balance;                 // 账户余额

    // 基本信息
    private String realName;
    private String iphone;           // 脱敏手机号
    private String email;
    private String drivingLicense;

    //需要自己生成
    private String level;           // 会员等级（如“钻石会员”）
    private String membership;       // 会员等级详情，如“钻石会员 · 成长值4580”

    public void buildProfile() {
        level = points.toString();
        membership = "成长值: " + points;
    }
}