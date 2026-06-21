package com.suse.campus_rent.dto.app;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExtendOrderDTO {
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotNull(message = "延长天数不能为空")
    @Min(value = 1, message = "延长天数至少为1天")
    private Integer extraDays;
}