package com.suse.campus_rent.vo.admin;

import com.suse.campus_rent.core.PriceResult;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailVO {
    private String orderNo;
    private String status;
    private LocalDateTime createTime;
    private String paymentMethod;

    // 用户信息（快照 + 最新身份证/驾驶证）
    private String userName;
    private String userPhone;
    private String idCard;
    private String licenseNo;

    // 车辆信息
    private String vehicleName;
    private String plate;
    private String vehicleType;      // 车型名称（如 SUV）
    private String color;

    // 租赁信息
    private LocalDateTime pickupTime;    // 取车时间 = rentStartTime
    private LocalDateTime returnTime;    // 预计还车时间 = rentEndTime
    private Integer days;
    private String pickupLocation;
    private String returnLocation;

    private BigDecimal deposit;               // 押金金额
    private String depositStatus;             // 押金状态：未收取、已收取、已退还
    private List<ServiceVO> services;          // 增值服务列表
    private String userRole;                   // 用户角色
    private BigDecimal discount;               // 折扣金额
    private BigDecimal carSubtotal;            // 车辆租金小计
    private BigDecimal servicesSubtotal;       // 服务费用小计
    private BigDecimal penaltyAmount;
    private String penaltyReason;  // 罚金原因

    // 费用
    private BigDecimal amount;           // 订单总金额（含押金）
    // 日志
    private List<OrderLogVO> logs;

    private PriceResult priceResult;
    private List<PenaltyItemVO> penalties;    // 罚金明细列表
}

