package com.suse.campus_rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.dto.admin.OrderQueryDTO;
import com.suse.campus_rent.entity.Order;
import com.suse.campus_rent.vo.admin.OrderVO;
import com.suse.campus_rent.vo.admin.RevenueTrendVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * OrderMapper
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/2 00:24
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 分页查询订单列表（关联车辆、车型）
     */
    IPage<OrderVO> selectOrderList(Page<?> page, @Param("query") OrderQueryDTO query);

    /**
     * 统计各状态订单数量（用于卡片）
     */
    Map<String, Object> selectStatistics();

    // 统计今日订单数
    @Select("SELECT COUNT(*) FROM `order` WHERE  DATE(create_time) = CURDATE()")
    Integer selectTodayOrderCount();

    // 统计今日已完成订单总金额（营收）
    @Select("SELECT COALESCE(SUM(total_amount), 0) FROM `order` WHERE status = '已完成' AND DATE(create_time) = CURDATE()")
    BigDecimal selectTodayRevenue();


    // 获取近30天订单量趋势
    @Select("SELECT " +
            "    DATE(create_time) AS date, " +
            "    COUNT(*) AS value " +
            "FROM `order` " +
            "WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) " +
            "GROUP BY DATE(create_time) " +
            "ORDER BY date")
    List<Map<String, Object>> selectOrderTrendLast30Days();

    // 获取近30天营收趋势
    @Select("SELECT " +
            "    DATE(create_time) AS date, " +
            "    COALESCE(SUM(total_amount), 0) AS value " +
            "FROM `order` " +
            "WHERE status = '已完成' AND create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) " +
            "GROUP BY DATE(create_time) " +
            "ORDER BY date")
    List<Map<String, Object>> selectRevenueTrendLast30Days();

    // 统计逾期订单数（假设状态为'已逾期'或根据时间判断，这里假设状态字段）
    Integer selectOverdueOrderCount();

    // 在 OrderMapper 中添加以下方法
    List<Map<String, Object>> selectLatestOrdersWithUserAndCar(@Param("limit") int limit);
}
