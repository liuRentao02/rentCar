package com.suse.campus_rent.dto.admin;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CarInfoUpdateDTO {

    @NotNull(message = "车辆ID不能为空")
    private Long carId;

    private Long modelId;

    private String plateNumber;

    private String vinCode;

    private String engineNo;

    private String vehicleColor;

    private Long shopId;

    @Min(value = 0, message = "里程不能小于0")
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
