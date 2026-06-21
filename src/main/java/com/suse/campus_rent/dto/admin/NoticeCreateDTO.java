package com.suse.campus_rent.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoticeCreateDTO {

    private Long userId = -1L;

    @NotBlank(message = "标题不能为空")
    @Size(min = 3, max = 100, message = "标题长度3-100")
    private String title;

    @NotBlank(message = "内容不能为空")
    @Size(min = 10, max = 5000, message = "内容长度10-5000")
    private String content;

    @NotBlank(message = "类型不能为空")
    private String type;      // system, activity, maintenance, other

    private String status;    // draft, published  默认 draft
    private String priority;  // normal, important, urgent

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireTime; //过期时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime scheduledTime;  // 定时发布时间，仅当 status = published 且不为空时有效

    private List<Long> attachmentIds;     // 关联的附件ID列表
}