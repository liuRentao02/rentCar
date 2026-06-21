package com.suse.campus_rent.dto.common;

import lombok.Data;

/**
 * QueryDTO
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/28 20:24
 */
@Data
public class QueryDTO {
    private Integer page = 1;
    private Integer size = 10;
}
