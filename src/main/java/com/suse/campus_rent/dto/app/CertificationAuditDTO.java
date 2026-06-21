package com.suse.campus_rent.dto.app;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CertificationAuditDTO {
    @NotNull(message = "认证记录ID不能为空")
    private Long id;
    @NotNull(message = "审核状态不能为空")
    private Integer status;      // 1-通过，2-拒绝
    private String remark;       // 备注
}