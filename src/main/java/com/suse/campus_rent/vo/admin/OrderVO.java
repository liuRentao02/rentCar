package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderVO {
    private Long id;
    private String orderNo;           // 订单号，不带#
    private String userName;           // 联系人姓名（快照）
    private String userPhone;          // 联系人电话（快照）
    private String vehicleName;        // 车辆名称
    private String plate;              // 车牌号
    private String rentalPeriod;       // 租期格式化字符串 "YYYY-MM-DD HH:mm ~ YYYY-MM-DD HH:mm"
    private Integer days;              // 总天数
    private BigDecimal amount;         // 订单总金额
    private String status;             // 状态码：pendingPayment, paid, pendingPickup, renting, pendingReturn, completed, cancelled, abnormal
    private String userAvatarColor;    // 头像颜色（前端计算，可不返回）
}