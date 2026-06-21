package com.suse.campus_rent.vo.admin;

import lombok.Data;

@Data
public class SystemConfigVO {
    // 平台信息
    private String platformName;
    private String platformText;
    private String platformUrl;
    private String platformEmail;
    private String platformPhone;
    private String platformAddress;
    private String platformWorkTime;
    private String footerText;
    private String logo;
}