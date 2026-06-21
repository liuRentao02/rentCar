package com.suse.campus_rent.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class AdminConfirmReturnDTO {
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    private List<PenaltyItemDTO> penalties;  // 罚金列表

    private String description; // 可为空
}