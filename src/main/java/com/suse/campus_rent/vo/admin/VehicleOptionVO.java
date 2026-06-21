package com.suse.campus_rent.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleOptionVO {
    private Long value;      // 车辆ID
    private String label;    // 显示文本：车牌 + 车型
}