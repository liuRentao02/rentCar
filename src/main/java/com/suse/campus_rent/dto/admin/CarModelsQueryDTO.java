package com.suse.campus_rent.dto.admin;

import com.suse.campus_rent.dto.common.QueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CarModelsQueryDTO extends QueryDTO {
    private String brandName;
    private String seriesName;
    private String modelName;
    private String category;
    private String energyType;
    private Integer status;
}