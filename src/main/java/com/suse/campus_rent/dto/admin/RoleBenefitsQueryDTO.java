package com.suse.campus_rent.dto.admin;

import com.suse.campus_rent.dto.common.QueryDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleBenefitsQueryDTO extends QueryDTO {
    private String role;      // 角色标识模糊查询
}