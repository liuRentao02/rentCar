package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MaintenanceRecordVO {
    private Long id;
    private String plate;           // 车牌号
    private Long vehicleId;
    private String model;           // 车型名称
    private String maintenanceItem;
    private BigDecimal cost;
    private LocalDate maintenanceDate;
    private String status;
    private String remarks;
}