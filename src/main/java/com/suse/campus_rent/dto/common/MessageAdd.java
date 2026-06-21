package com.suse.campus_rent.dto.common;

import lombok.Data;

/**
 * MessageAdd
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/4/16 21:20
 */
@Data
public class MessageAdd {
    private Long id;
    private Long toId;
    private Long conversationId;
    private Integer type;
    private String content;
}
