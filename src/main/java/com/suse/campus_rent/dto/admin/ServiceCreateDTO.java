package com.suse.campus_rent.dto.admin;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Data
public class ServiceCreateDTO {
    @NotBlank(message = "服务名称不能为空")
    private String name;

    private String description;

    @NotNull(message = "单价不能为空")
    private BigDecimal price;

    @NotBlank(message = "服务类型不能为空")
    private String type;

    private Integer status = 1; // 默认上架
}