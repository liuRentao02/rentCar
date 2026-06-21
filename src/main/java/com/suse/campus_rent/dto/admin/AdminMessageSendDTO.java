package com.suse.campus_rent.dto.admin;

import lombok.Data;

@Data
public class AdminMessageSendDTO {
    private Long conversationId;
    private Integer type;
    private String content;
}