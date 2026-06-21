package com.suse.campus_rent.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * PriceResult
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/4/9 10:06
 */
@Data
public class PriceResult {
    // 原始总价（优惠前）
    private BigDecimal originalTotal = BigDecimal.ZERO;

    // 最终实付金额（优惠后）
    private BigDecimal finalTotal = BigDecimal.ZERO;

    // 费用明细列表（每一项都是加钱项）
    private List<FeeItem> feeItems;

    // 优惠明细列表（每一项都是减钱项）
    private List<DiscountItem> discountItems;

    // 便捷方法：累加费用
    public void addFeeItem(String key, String description, BigDecimal amount) {
        if (this.feeItems == null) {
            this.feeItems = new ArrayList<>();
        }
        this.feeItems.add(new FeeItem(key, description, amount));
        this.originalTotal = this.originalTotal.add(amount);
        this.finalTotal = this.finalTotal.add(amount);
    }

    // 便捷方法：应用优惠
    public void addDiscountItem(String key, String description, BigDecimal amount) {
        if (this.discountItems == null) {
            this.discountItems = new ArrayList<>();
        }
        this.discountItems.add(new DiscountItem(key, description, amount));
        this.finalTotal = this.finalTotal.subtract(amount);
    }
}

