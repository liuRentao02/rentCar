package com.suse.campus_rent.dto.app;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Data
public class OrderEstimateDTO {
    @NotNull(message = "车辆ID不能为空")
    private Long carId;

    @NotNull(message = "租用天数不能为空")
    private Integer totalDays;

    private List<Long> serviceIds;  // 可选服务ID列表
}