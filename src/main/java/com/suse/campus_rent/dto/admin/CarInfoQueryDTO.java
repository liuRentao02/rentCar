package com.suse.campus_rent.dto.admin;

import com.suse.campus_rent.dto.common.QueryDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarInfoQueryDTO extends QueryDTO {
    private String keyword;       // 车牌号/车架号模糊搜索
    private String brand;         // 品牌名称（通过车型关联查）
    private String energyType;    // 能源类型（通过车型关联查）
    private String status;        // 车辆状态：available/rented/maintenance/offline
    private Long modelId;         // 车型ID精确筛选
    private Long shopId;          // 门店ID筛选
}
