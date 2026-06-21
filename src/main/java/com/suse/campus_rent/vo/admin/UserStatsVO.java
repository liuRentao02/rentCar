package com.suse.campus_rent.vo.admin;

import lombok.Data;

@Data
public class UserStatsVO {
    private Integer monthlyOperations;   // 本月操作数
    private Integer loginCount;           // 总登录次数
    private Integer createdOrders;        // 创建订单数
    private Integer processedTickets;     // 处理工单数（若无工单系统可设为0）
}