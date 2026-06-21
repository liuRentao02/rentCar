package com.suse.campus_rent.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.suse.campus_rent.vo.admin.DashboardStatsVO;
import com.suse.campus_rent.vo.admin.DashboardVO;
import com.suse.campus_rent.entity.MaintenanceRecord;
import com.suse.campus_rent.entity.UserCertification;
import com.suse.campus_rent.mapper.*;
import com.suse.campus_rent.service.admin.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service("adminDashboardService")
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final CarInfoMapper carInfoMapper;
    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final UserCertificationMapper userCertificationMapper;
    private final MaintenanceRecordMapper maintenanceRecordMapper;

    @Override
    public DashboardVO getDashboardData() {
        log.info("开始获取首页仪表盘数据");
        DashboardVO dashboardVO = new DashboardVO();

        // 1. 核心指标
        dashboardVO.setStatistics(buildStats());

        // 4. 趋势数据（近30天）
        dashboardVO.setOrderTrend(fillDateGaps(orderMapper.selectOrderTrendLast30Days()));
        dashboardVO.setRevenueTrend(fillDateGaps(orderMapper.selectRevenueTrendLast30Days()));

        // 5. 车辆出租率
        dashboardVO.setRentRate(buildRentRate());

        // 6. 最新动态
        dashboardVO.setLatestOrders(orderMapper.selectLatestOrdersWithUserAndCar(10));
        dashboardVO.setLatestUsers(userMapper.selectLatestUsersWithCert(10));

        log.info("首页仪表盘数据获取完成");
        return dashboardVO;
    }

    // ==================== 私有方法：数据构建 ====================

    private DashboardStatsVO buildStats() {
        DashboardStatsVO stats = new DashboardStatsVO();

        // 用户数据
        stats.setTotalUsers((int) userMapper.countAll());
        stats.setTodayNewUsers(userMapper.selectTodayNewUsers() != null ? userMapper.selectTodayNewUsers() : 0);

        // 车辆数据
        Integer totalCars = carInfoMapper.selectTotalCars();
        Integer rentedCars = carInfoMapper.selectRentedCars();
        stats.setTotalVehicles(totalCars);
        stats.setAvailableVehicles(totalCars - rentedCars);

        // 订单数据
        stats.setTodayOrders(orderMapper.selectTodayOrderCount() != null ? orderMapper.selectTodayOrderCount() : 0);
        stats.setTodayRevenue(orderMapper.selectTodayRevenue() != null ? orderMapper.selectTodayRevenue() : BigDecimal.ZERO);

        // 待办工单总数（多个待办类别合计）
        long pendingAuth = userCertificationMapper.selectCount(
                new LambdaQueryWrapper<UserCertification>().eq(UserCertification::getStatus, 0));
        long pendingRepair = maintenanceRecordMapper.selectCount(
                new LambdaQueryWrapper<MaintenanceRecord>().eq(MaintenanceRecord::getStatus, "pending"));

        long overdueOrders = orderMapper.selectOverdueOrderCount();
        stats.setTodoCount((int) (pendingAuth + pendingRepair + overdueOrders));

        stats.setOverdueOrders((int) overdueOrders);
        return stats;
    }

    private List<Map<String, Object>> buildRentRate() {
        List<Map<String, Object>> list = new ArrayList<>();
        Integer total = carInfoMapper.selectTotalCars();
        Integer rented = carInfoMapper.selectRentedCars();
        Integer available = total - rented;

        long repairing = maintenanceRecordMapper.selectCount(
                new LambdaQueryWrapper<MaintenanceRecord>().eq(MaintenanceRecord::getStatus, "pending")
                        .or().eq(MaintenanceRecord::getStatus, "ongoing"));

        addRateItem(list, "在租", rented);
        addRateItem(list, "空闲", available);
        addRateItem(list, "维修", (int) repairing);
        return list;
    }

    private void addRateItem(List<Map<String, Object>> list, String name, Integer value) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("value", value);
        list.add(map);
    }

    /**
     * 填充近30天缺失日期
     */
    private List<Map<String, Object>> fillDateGaps(List<Map<String, Object>> dbData) {
        Map<String, Object> dataMap = new HashMap<>();
        if (dbData != null) {
            for (Map<String, Object> row : dbData) {
                dataMap.put(row.get("date").toString(), row.get("value"));
            }
        }
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 29; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String dateStr = date.toString();
            Map<String, Object> item = new HashMap<>();
            item.put("date", dateStr);
            item.put("value", dataMap.getOrDefault(dateStr, 0));
            result.add(item);
        }
        return result;
    }
}