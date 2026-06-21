package com.suse.campus_rent.vo.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class CarVO {
    // ====== 前端最终展示字段 ======
    private Long carId;
    private String brandName;
    private String imageUrls;

    private String category;
    private Map<String, String> specs;
    private BigDecimal dailyRent;

    // ====== 组装所需的底层原始数据 ======
    @JsonIgnore
    private String energyType;
    @JsonIgnore
    private String displacement;
    @JsonIgnore
    private Integer seatCount;
    @JsonIgnore
    private String gearboxType;

    public void buildSpecs() {
        Map<String, String> specMap = new HashMap<>();

        // 1. 处理续航/排量
        if ("纯电动".equals(this.energyType)) {
            String disc = StringUtils.hasText(this.displacement) ? this.displacement + "KM" : "0KM";
            specMap.put("range", "⚡ " + disc);
        } else if ("人力".equals(this.energyType)) {
            specMap.put("range", "\uD83E\uDDB5 人力");
        } else {
            String disc = StringUtils.hasText(this.displacement) ? this.displacement + "L" : "0L";
            specMap.put("range", "⛽ " + disc);
        }

        // 2. 处理座位
        String seatStr = (this.seatCount != null) ? this.seatCount + "座" : "0座";
        specMap.put("seats", "👤 " + seatStr);

        // 3. 处理变速箱
        String transStr = (this.gearboxType != null) ? this.gearboxType : "自动";
        specMap.put("transmission", "⚙️ " + transStr);

        this.specs = specMap;
    }
}
