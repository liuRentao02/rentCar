package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServiceVO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String type;
    private Integer status;  // 0-下架，1-上架
}