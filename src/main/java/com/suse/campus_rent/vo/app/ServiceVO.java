package com.suse.campus_rent.vo.app;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServiceVO {
    private Long id;
    private String name;
    private BigDecimal price;
}