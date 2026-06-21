package com.suse.campus_rent.vo.app;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderListItemVO {
    private Long id;
    private String orderNo;
    private String carName;
    private String carImage;
    private LocalDateTime rentStartTime;
    private LocalDateTime rentEndTime;
    private Integer totalDays;
    private BigDecimal totalAmount;
    private String status;
    private String statusClass;
    private String statusText;
}