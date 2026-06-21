package com.suse.campus_rent.dto.admin;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Data
public class ReviewStatusDTO {
    private List<Long> ids;        // 批量操作时使用
    private Long id;               // 单条操作时使用
    @NotNull(message = "状态不能为空")
    private Integer status;        // 0-隐藏，1-显示
}