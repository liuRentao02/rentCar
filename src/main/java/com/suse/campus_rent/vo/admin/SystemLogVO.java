package com.suse.campus_rent.vo.admin;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * SystemLogVO
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/4/2 11:04
 */
@Data
public class SystemLogVO {
    private Long id;
    private String level;
    private String category;
    private String content;
    private String detail;
    private String realName;
    private String avatar;
    private String username;
    private LocalDateTime createTime;
    private int isDeleted = 0;
}
