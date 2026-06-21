package com.suse.campus_rent.dto.admin;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Data
public class ServiceUpdateDTO {
    @NotNull(message = "服务ID不能为空")
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private String type;
    private Integer status;
}