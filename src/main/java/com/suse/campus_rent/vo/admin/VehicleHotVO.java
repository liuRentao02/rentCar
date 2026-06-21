package com.suse.campus_rent.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleHotVO {
    private Long carId;
    private String plateNumber;
    private String brandName;
    private String modelName;
    private BigDecimal dailyRent;
    private Long orderCount;   // 租赁次数
}