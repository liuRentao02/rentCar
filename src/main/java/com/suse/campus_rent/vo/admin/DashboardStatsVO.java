package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardStatsVO {
    private Integer totalUsers;         // 总用户数
    private Integer todayNewUsers;      // 今日新增用户
    private Integer totalVehicles;      // 总车辆数
    private Integer availableVehicles;  // 空闲车辆数
    private Integer todayOrders;         // 今日订单数
    private BigDecimal todayRevenue;     // 今日营收
    private Integer todoCount;           // 待办工单数
    private Integer overdueOrders;       // 逾期订单数
}