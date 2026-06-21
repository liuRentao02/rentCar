package com.suse.campus_rent.dto.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PenaltyItemDTO {
    private String reason;   // 罚金原因
    private BigDecimal amount; // 罚金金额
}