package com.suse.campus_rent.vo.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewListVO {
    private Long id;
    private String vehicleInfo;      // 品牌+型号
    private String userName;         // 用户昵称/姓名
    private Integer rating;
    private String content;
    private String images;           // 图片URL，逗号分隔
    private Boolean hasImage;        // 是否有图片
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private Integer status;          // 0隐藏，1显示
    private Long orderId;            // 关联订单ID
}