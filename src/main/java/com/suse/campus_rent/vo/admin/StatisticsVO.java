package com.suse.campus_rent.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsVO {
    private long all;
    private long active;
    private long inactive;
    private long admin;
}