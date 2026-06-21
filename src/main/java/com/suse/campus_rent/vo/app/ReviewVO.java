package com.suse.campus_rent.vo.app;

import lombok.Data;

@Data
public class ReviewVO {
    private Long id;
    private String name;          // 用户昵称
    private String userType;       // 用户类型（如“商务旅客”，暂用role代替）
    private String avatar;         // 用户头像
    private Integer rating;        // 评分
    private String vehicle;        // 车型分类（用于筛选，如suv/sedan等）
    private String carModel;       // 车辆型号名称
    private String carName;
    private String carImage;
    private String content;        // 评价内容
    private String image;           // 评论图片
    private String date;           // 发布日期（yyyy-MM-dd）
    private Integer likes;         // 点赞数
    private Integer comments;      // 评论数
    private Boolean featured;      // 是否为精选
    private Boolean liked;         // 当前用户是否已点赞
    private Integer status;
}