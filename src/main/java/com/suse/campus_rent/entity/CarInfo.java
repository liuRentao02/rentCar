package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 单车档案表 car_info
 * 存储具体的每一辆实物车信息
 */
@Data
@TableName("car_info")
public class CarInfo {

    /**
     * 单车主键ID
     */
    @TableId(value = "car_id", type = IdType.AUTO)
    private Long carId;

    /**
     * 关联车型ID
     */
    private Long modelId;

    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 车架号/VIN
     */
    private String vinCode;

    /**
     * 发动机号
     */
    private String engineNo;

    /**
     * 车身颜色（关联字典：dict_color）
     */
    private String vehicleColor;

    /**
     * 所属门店ID
     */
    private Long shopId;

    /**
     * 当前里程数（公里）
     */
    private Integer currentMileage;

    /**
     * 当前油量/电量百分比
     */
    private BigDecimal currentFuel;

    /**
     * 上牌日期
     */
    private LocalDate licenseDate;

    /**
     * 保险到期日
     */
    private LocalDate insuranceExpiry;

    /**
     * 年检到期日
     */
    private LocalDate inspectionExpiry;

    /**
     * 日租金（元）
     */
    private BigDecimal dailyRent;

    /**
     * 押金（元）
     */
    private BigDecimal depositAmount;

    /**
     * 累计租赁次数
     */
    private Integer rentalCount;

    /**
     * 车辆状态（关联字典：dict_car_status）
     */
    private String status;

    /**
     * 车辆图片集（json数组）
     */
    private String imageUrls;

    /**
     * 入库时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
