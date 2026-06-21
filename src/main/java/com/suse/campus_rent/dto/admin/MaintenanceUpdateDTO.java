package com.suse.campus_rent.dto.admin;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MaintenanceUpdateDTO {
    @NotNull(message = "记录ID不能为空")
    private Long id;

    private Long vehicleId;

    private String maintenanceItem;

    @DecimalMin(value = "0.0", inclusive = false, message = "费用必须大于0")
    private BigDecimal cost;

    private LocalDate maintenanceDate;

    private String status;

    private String remarks;
}