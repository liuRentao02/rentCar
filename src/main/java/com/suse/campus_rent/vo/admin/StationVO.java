package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StationVO {
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