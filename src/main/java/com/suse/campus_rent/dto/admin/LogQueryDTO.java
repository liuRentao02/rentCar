package com.suse.campus_rent.dto.admin;

import com.suse.campus_rent.dto.common.QueryDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * LogQueryDTO
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/4/2 11:44
 */
@Getter
@Setter
public class LogQueryDTO extends QueryDTO {
    private String keyword;

    private LocalDate startTime;
    private LocalDate endTime;

    private String type;
    private String level;
}
