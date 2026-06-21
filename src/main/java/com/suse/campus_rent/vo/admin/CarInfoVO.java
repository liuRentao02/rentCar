package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CarInfoVO {
    private Long carId;
    private Long modelId;
    private String plateNumber;
    private String vinCode;
    private String engineNo;
    private String vehicleColor;
    private Long shopId;
    private Integer currentMileage;
    private BigDecimal currentFuel;
    private LocalDate licenseDate;
    private LocalDate insuranceExpiry;
    private LocalDate inspectionExpiry;
    private BigDecimal dailyRent;
    private BigDecimal depositAmount;
    private Integer rentalCount;
    private String status;
    private String image;

    // 关联查询字段（非数据库字段）
    private String modelName;
    private String brandName;
    private String energyType;
}
