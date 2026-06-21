package com.suse.campus_rent.dto.admin;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Data
public class StationCreateDTO {
    @NotBlank(message = "网点名称不能为空")
    private String name;
    @NotBlank(message = "地址不能为空")
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String contactPhone;
    private String businessHours;
    private Integer status = 1;
    private Integer sortOrder = 0;
}