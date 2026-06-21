package com.suse.campus_rent.dto.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarModelsUpdateDTO {
    private Long modelId; // 前端传过来的主键ID

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
}
