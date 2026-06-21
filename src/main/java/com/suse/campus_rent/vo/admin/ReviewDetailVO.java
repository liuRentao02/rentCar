package com.suse.campus_rent.vo.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewDetailVO {
    private Long id;
    private String vehicleInfo;
    private String userName;
    private String userPhone;
    private Integer rating;
    private String content;
    private List<String> imageUrls;   // 图片列表
    private String images;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private Integer status;
    private Long orderId;
    private String orderNo;           // 关联订单号
    private Integer likesCount;
    private Integer commentsCount;
    private Integer featured;         // 是否精选
}