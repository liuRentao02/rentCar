package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户评价表实体类
 */
@Data
@TableName("review")
public class Review {
    @TableId(type = IdType.AUTO)
    private Long id; // 评价ID

    private Long userId; // 评价用户ID

    private Long carId; // 关联车辆ID

    private Long orderId; // 关联订单ID（可选）

    private Integer rating; // 评分：1-5星

    private String content; // 评价内容

    private String images; // 评价图片URL，多个用逗号分隔

    private Integer likesCount; // 点赞数

    private Integer commentsCount; // 评论数

    private Integer status; // 状态：0-隐藏，1-显示

    private Integer featured; // 是否为精选评价

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}