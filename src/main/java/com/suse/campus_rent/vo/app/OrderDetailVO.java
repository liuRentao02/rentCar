package com.suse.campus_rent.vo.app;

import com.suse.campus_rent.core.PriceResult;
import com.suse.campus_rent.entity.OrderServices;
import com.suse.campus_rent.entity.Services;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailVO {
    private Long id;
    private String orderNo;
    private LocalDateTime createTime;
    private String statusClass;
    private String statusText;
    private Boolean canCancel;
    private Boolean canPay;
    private Boolean canApplyPickup;
    private Boolean canApplyReturn;
    private int extension = 0;

    // 车辆信息
    private String carName;
    private String carImage;
    private List<String> carDetails;
    private BigDecimal dailyRent;

    // 行程信息
    private String pickupLocation;
    private String returnLocation;
    private LocalDateTime rentStartTime;
    private LocalDateTime rentEndTime;
    private Integer totalDays;

    // 增值服务
    private List<OrderServices> services;

    // 费用明细
    private BigDecimal carSubtotal;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal totalAmount;

    // 联系人信息
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private String role;

    private BigDecimal penaltyAmount;          // 罚金（已有，但确保有 getter/setter）
    private String penaltyReason;  // 罚金原因
    private Integer finalCarState;             // 检测后车辆状态
    private String finalCarStateDesc;          // 状态描述（可租/维修中）

    private BigDecimal depositOriginal;       // 押金原价
    private BigDecimal depositDiscountRate;   // 押金折扣率
    private BigDecimal depositPayable;        // 应付押金
    private String depositStatus;        // 押金状态（可选，用于前端显示，如“已支付”、“待支付”）
    private BigDecimal depositRefundAmount;

    private Boolean reviewed;

    PriceResult priceResult;
}