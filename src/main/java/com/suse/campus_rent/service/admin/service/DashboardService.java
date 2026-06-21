package com.suse.campus_rent.service.admin.service;

import com.suse.campus_rent.vo.admin.DashboardVO;

public interface DashboardService {
    /**
     * 获取首页仪表盘完整数据
     */
    DashboardVO getDashboardData();
}