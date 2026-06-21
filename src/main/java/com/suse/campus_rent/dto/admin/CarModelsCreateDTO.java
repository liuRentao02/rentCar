package com.suse.campus_rent.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarModelsCreateDTO {
    @NotBlank(message = "品牌不能为空")
    private String brandName;

    @NotBlank(message = "车系不能为空")
    private String seriesName;

    @NotBlank(message = "型号名称不能为空")
    private String modelName;

    private String vehicleLevel;
    private String category;
    private String energyType;
    private String emissionStandard;
    private String gearboxType;
    private String driveMode;
    private String fuelGrade;
    private String displacement;

    @NotNull(message = "座位数不能为空")
    private Integer seatCount;

    private String sunroofType;
    private String interiorColor;
    private BigDecimal guidePrice;
    private Integer status;
}