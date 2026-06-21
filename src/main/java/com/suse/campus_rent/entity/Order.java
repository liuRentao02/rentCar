package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 租赁订单表实体类
 */
@Data
@TableName("`order`")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id; // 订单ID，主键

    private String orderNo; // 对外展示的订单号，唯一

    private Long userId; // 租车用户ID

    private Long carId; // 租赁车辆ID

    private String roleAtRental; // 租车时的用户角色快照

    private BigDecimal dailyRentSnapshot; // 租车时的日租金快照

    private BigDecimal depositSnapshot; // 租车时的押金快照

    private BigDecimal rentAmount;//租金

    private String contactName; // 联系人姓名

    private String contactPhone; // 联系电话

    private String contactEmail; // 联系邮箱

    private String pickupLocation; // 取车地点

    private String returnLocation; // 还车地点

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rentStartTime; // 租赁开始时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rentEndTime; // 预计归还时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualReturnTime; // 实际归还时间

    private Integer totalDays; // 租用天数

    private BigDecimal totalAmount; // 订单总金额

    private BigDecimal overdueFee; // 超时费

    private BigDecimal penaltyAmount; // 罚金

    private String inspectionDescription;//原因

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime; // 支付时间

    @TableField("status")
    private String status;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime; // 最后更新时间

    private String paymentMethod;

    //车辆地状态
    private Integer final_car_state;
    //取车时地状态
    private String inspection_description;
}