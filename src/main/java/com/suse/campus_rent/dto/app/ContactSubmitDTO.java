package com.suse.campus_rent.dto.app;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContactSubmitDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    private String subject;   // 可选，默认“其他”

    @NotBlank(message = "留言内容不能为空")
    private String message;
}