package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("maintenance_record")
public class MaintenanceRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long vehicleId;             // 车辆ID

    private String maintenanceItem;      // 维修项目

    private BigDecimal cost;              // 维修费用

    private LocalDate maintenanceDate;    // 维修日期

    private String status;                // 状态：pending, ongoing, completed

    private String remarks;                // 备注

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}