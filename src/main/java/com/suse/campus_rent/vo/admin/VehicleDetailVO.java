package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehicleDetailVO {
    private String model;           // 车型名称
    private Integer rentalCount;     // 租赁次数
    private BigDecimal totalRevenue; // 总营收
    private BigDecimal avgDailyRent; // 平均日租金
}