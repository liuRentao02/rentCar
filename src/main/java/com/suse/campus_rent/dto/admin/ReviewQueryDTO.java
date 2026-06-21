package com.suse.campus_rent.dto.admin;

import com.suse.campus_rent.dto.common.QueryDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewQueryDTO extends QueryDTO {
    private String keyword;          // 关键词：车辆名称、评论内容、用户名
    private Integer rating;          // 星级别精确筛选（1-5），可选
    private Integer status;          // 状态：0-隐藏，1-显示，null表示全部
    private String type;             // 类型：car / noncar / all（用于区分汽车/非机动车）
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime; // 评论时间范围开始
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;   // 评论时间范围结束
}