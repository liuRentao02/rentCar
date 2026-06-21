package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DashboardVO {
    private DashboardStatsVO statistics;              // 核心指标
    private List<DashboardTodoVO> todos;              // 待办事项
    private List<DashboardWarningVO> warnings;        // 预警告警
    private List<Map<String, Object>> orderTrend;     // 近30天订单趋势
    private List<Map<String, Object>> revenueTrend;   // 近30天营收趋势
    private List<Map<String, Object>> rentRate;       // 车辆出租率
    private List<Map<String, Object>> latestOrders;   // 最新订单
    private List<Map<String, Object>> latestUsers;    // 最新用户
}