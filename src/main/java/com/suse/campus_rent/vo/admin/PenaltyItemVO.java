package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PenaltyItemVO {
    private String reason;
    private BigDecimal amount;
}
