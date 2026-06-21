package com.suse.campus_rent.dto.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderSubmitDTO {
    @NotNull(message = "车辆ID不能为空")
    private Long carId;

    @NotNull(message = "取车地点不能为空")
    private String pickupLocation;

    @NotNull(message = "还车地点不能为空")
    private String returnLocation;

    @NotNull(message = "取车时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rentStartTime;

    @NotNull(message = "还车时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rentEndTime;

    @NotNull(message = "租用天数不能为空")
    @Positive(message = "租用天数必须大于0")
    private Integer totalDays;

    // 前端传入的服务ID列表
    private List<Long> serviceIds;
}