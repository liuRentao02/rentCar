package com.suse.campus_rent.dto.admin;

import com.suse.campus_rent.dto.common.QueryDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class OrderQueryDTO extends QueryDTO {
    private String orderNo;          // 订单号（不带#）
    private String userInfo;          // 用户姓名/手机号模糊查询
    private String status;            // 状态码，如 pendingPayment
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;      // 订单创建开始日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;         // 订单创建结束日期
}