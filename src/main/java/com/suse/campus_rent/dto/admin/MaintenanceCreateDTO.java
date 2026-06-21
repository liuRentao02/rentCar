package com.suse.campus_rent.dto.admin;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MaintenanceCreateDTO {
    @NotNull(message = "车辆ID不能为空")
    private Long vehicleId;

    @NotBlank(message = "维修项目不能为空")
    private String maintenanceItem;

    @NotNull(message = "维修费用不能为空")
    @DecimalMin(value = "0.0", inclusive = false, message = "费用必须大于0")
    private BigDecimal cost;

    @NotNull(message = "维修日期不能为空")
    private LocalDate maintenanceDate;

    @NotBlank(message = "状态不能为空")
    private String status;      // pending/ongoing/completed

    private String remarks;
}