package com.suse.campus_rent.dto.admin;

import com.suse.campus_rent.dto.common.QueryDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceQueryDTO extends QueryDTO {
    private String name;      // 服务名称模糊查询
    private String type;       // 服务类型
    private Integer status;    // 状态：0-下架，1-上架
}