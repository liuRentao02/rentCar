package com.suse.campus_rent.dto.admin;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CarInfoCreateDTO {

    @NotNull(message = "关联车型ID不能为空")
    private Long modelId;

    @NotBlank(message = "车牌号不能为空")
    private String plateNumber;

    @NotBlank(message = "车架号/VIN不能为空")
    private String vinCode;

    private String engineNo;

    @NotBlank(message = "车身颜色不能为空")
    private String vehicleColor;

    private Long shopId;

    private Integer currentMileage;

    private BigDecimal currentFuel;

    private LocalDate licenseDate;

    private LocalDate insuranceExpiry;

    private LocalDate inspectionExpiry;

    private BigDecimal dailyRent;

    private BigDecimal depositAmount;

    private String status;

    private String imageUrls;
}
