package com.suse.campus_rent.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoticeUpdateDTO {
    private Long id;  // 路径参数，但这里保留便于校验

    @NotBlank(message = "标题不能为空")
    @Size(min = 3, max = 100, message = "标题长度3-100")
    private String title;

    @NotBlank(message = "内容不能为空")
    @Size(min = 10, max = 5000, message = "内容长度10-5000")
    private String content;

    @NotBlank(message = "类型不能为空")
    private String type;

    private String status;    // draft, published
    private String priority;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime scheduledTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireTime;

    private List<Long> attachmentIds;
}