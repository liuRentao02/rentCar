package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 车型配置表 car_model
 * 存储车辆的出厂标准规格信息（模板）
 */
@Data
@TableName("car_models")
public class CarModels {

    /**
     * 车型主键ID
     */
    @TableId(value = "model_id", type = IdType.AUTO)
    private Long modelId;

    /**
     * 品牌名称（如：丰田）
     */
    private String brandName;

    private String vehicleType;   // 新增，对应数据库 vehicle_type

    /**
     * 车系名称（如：凯美瑞）
     */
    private String seriesName;

    /**
     * 具体型号全称（如：2022款 2.5S 锋尚版）
     */
    private String modelName;

    /**
     * 车辆级别（关联字典：dict_vehicle_level）
     */
    private String vehicleLevel;

    /**
     * 车辆类别（关联字典：dict_category）
     */
    private String category;

    /**
     * 能源类型（关联字典：dict_energy_type）
     */
    private String energyType;

    /**
     * 排放标准（关联字典：dict_emission）
     */
    private String emissionStandard;

    /**
     * 变速箱类型（关联字典：dict_gearbox）
     */
    private String gearboxType;

    /**
     * 驱动方式（关联字典：dict_drive）
     */
    private String driveMode;

    /**
     * 燃油标号（关联字典：dict_fuel_grade）
     */
    private String fuelGrade;

    /**
     * 排量/动力（如：2.5L）
     */
    private String displacement;

    /**
     * 座位数
     */
    private Integer seatCount;

    /**
     * 天窗类型（关联字典：dict_sunroof）
     */
    private String sunroofType;

    /**
     * 内饰颜色
     */
    private String interiorColor;

    /**
     * 厂商指导价（万元）
     */
    private BigDecimal guidePrice;

    /**
     * 车型状态（1-正常 0-停用）
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
