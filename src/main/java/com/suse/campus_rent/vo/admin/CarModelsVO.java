package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CarModelsVO {
    private Long modelId;
    private String brandName;
    private String seriesName;
    private String modelName;
    private String vehicleLevel;
    private String category;
    private String energyType;
    private String emissionStandard;
    private String gearboxType;
    private String driveMode;
    private String fuelGrade;
    private String displacement;
    private Integer seatCount;
    private String sunroofType;
    private String interiorColor;
    private BigDecimal guidePrice;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}