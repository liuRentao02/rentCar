package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_certification")
public class UserCertification {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String certType;      // student
    private String realName;
    private String studentId;
    private String school;
    private String idCard;
    private String imageUrls;      // 逗号分隔
    private Integer status;        // 0-待审核，1-通过，2-拒绝
    private String auditRemark;
    private LocalDateTime applyTime;
    private LocalDateTime auditTime;
    private Long auditUserId;
}