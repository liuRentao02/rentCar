package com.suse.campus_rent.dto.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StationUpdateDTO {
    private Long id;
    private String name;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String contactPhone;
    private String businessHours;
    private Integer status;
    private Integer sortOrder;
}