package com.suse.campus_rent.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 计费项与优惠项统一标识枚举
 * 用于 PriceResult 中 FeeItem 和 DiscountItem 的 key 字段
 *
 * @author LiuRentao
 * @since 2026/4/9
 */
@Getter
@AllArgsConstructor
public enum PriceKeys {

    // ========== 租金类费用（加钱项） ==========
    BASE_RENT("BASE_RENT", "基础租金"),
    BASE_RENT_DISCOUNT("BASE_RENT_DISCOUNT", "租金优惠价格"),
    SERVICE_FEE("SERVICE_FEE", "服务费"),

    // ========== 押金类（单独计费项） ==========
    DEPOSIT("DEPOSIT", "车辆押金"),
    DEPOSIT_DISCOUNT("DEPOSIT_DISCOUNT", "车辆押金优惠价格");

    /**
     * 唯一标识符（可用于数据库存储、日志、前端状态判断）
     */
    private final String key;

    /**
     * 中文展示名称（用于前端直接展示）
     */
    private final String displayName;

    /**
     * 根据 key 获取对应的枚举实例
     */
    public static PriceKeys fromKey(String key) {
        for (PriceKeys pk : values()) {
            if (pk.key.equals(key)) {
                return pk;
            }
        }
        return null;
    }
}