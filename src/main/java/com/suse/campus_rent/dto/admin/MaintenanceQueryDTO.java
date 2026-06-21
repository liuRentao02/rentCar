package com.suse.campus_rent.dto.admin;

import com.suse.campus_rent.dto.common.QueryDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class MaintenanceQueryDTO extends QueryDTO {
    private String plate;                // 车牌号模糊查询
    private String status;                // 状态
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateStart;          // 维修日期开始
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateEnd;             // 维修日期结束
}