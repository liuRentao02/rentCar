package com.suse.campus_rent.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueTrendVO {
    private String week;          // 周次，如"第一周"
    private BigDecimal amount;     // 营收金额
    private Integer percent;       // 占最大周金额的百分比
}