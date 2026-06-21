package com.suse.campus_rent.dto.admin;

import com.suse.campus_rent.dto.common.QueryDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StationQueryDTO extends QueryDTO {
    private String keyword;      // 名称/地址模糊搜索
    private Integer status;       // 状态筛选
}