package com.suse.campus_rent.vo.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class CarDetailVO {
    private Long carId;
    private String modelName;           // 车型名称
    private String subtitle;       // 副标题（品牌/分类/能源类型）
    private BigDecimal dailyRent;      // 日租金
    private List<String> image;   // 图片URL列表
    private List<Map<String, String>> keySpecs;  // 关键规格
    private List<Map<String, String>> techSpecs;  // 技术参数

    // ===== 以下是底层原始数据，用于构建展示字段 =====
    @JsonIgnore
    private String imageUrls;
    @JsonIgnore
    private BigDecimal depositAmount;    // 押金
    @JsonIgnore
    private String energyType;
    @JsonIgnore
    private String gearboxType;
    @JsonIgnore
    private String driveMode;
    @JsonIgnore
    private Integer seatCount;
    @JsonIgnore
    private String displacement;
    @JsonIgnore
    private String vehicleColor;
    @JsonIgnore
    private Integer currentMileage;

    public CarDetailVO() {
        this.keySpecs = new ArrayList<>();
        this.techSpecs = new ArrayList<>();
    }

    /**
     * 智能初始化方法：处理图片、副标题、规格参数
     */
    public void buildDetail() {
        // 1. 处理图片截取
        if (this.imageUrls == null || this.imageUrls.isEmpty()) {
            this.image = Collections.emptyList();
        } else {
            this.image = Arrays.asList(imageUrls.split(","));
        }

        this.subtitle = this.energyType != null ? this.energyType : "未知";

        // 3. 构建关键规格 (只展示用户最关心的)
        this.keySpecs = new ArrayList<>();
        if ("纯电动".equals(this.energyType)) {
            String range = (this.currentMileage != null && this.currentMileage > 0) ? this.currentMileage + "km续航" : "600km续航";
            addSpec("⚡", range);
        } else if ("人力".equals(this.energyType)) {
            addSpec("👤", "人力");
        } else {
            String disc = StringUtils.hasText(this.displacement) ? this.displacement : "0";
            addSpec("⛽", disc);
        }

        addSpec("👤", this.seatCount + "座");
        addSpec("⚙️", this.gearboxType);
        addSpec("🔋", this.driveMode);
        addSpec("🎨", this.vehicleColor);

        addTechSpec("能源类型", this.energyType);
        addTechSpec("变速箱", this.gearboxType);
        addTechSpec("驱动方式", this.driveMode);
        addTechSpec("座位数", this.seatCount != null ? this.seatCount + "座" : null);
        addTechSpec("排量", this.displacement);
        addTechSpec("车身颜色", this.vehicleColor);
        addTechSpec("当前里程", this.currentMileage != null ? this.currentMileage + " km" : null);
    }

    private void addSpec(String icon, String label) {
        if (label == null || label.trim().isEmpty()) return;
        Map<String, String> map = new LinkedHashMap<>();
        map.put("icon", icon);
        map.put("label", label);
        this.keySpecs.add(map);
    }

    // 小工具方法：添加技术参数（自动过滤空值）
    private void addTechSpec(String label, String value) {
        if (value != null && !value.trim().isEmpty()) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("label", label);
            map.put("value", value);
            this.techSpecs.add(map);
        }
    }
}
