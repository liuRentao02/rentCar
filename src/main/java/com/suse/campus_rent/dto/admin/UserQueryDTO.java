package com.suse.campus_rent.dto.admin;

import com.suse.campus_rent.dto.common.QueryDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class UserQueryDTO extends QueryDTO {
    private String keyword;              // 关键词（用户名/邮箱/手机号）
    private String role;                  // 角色
    private Integer state;                 // 状态：0-禁用，1-正常
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate registerDate;        // 注册日期（用于筛选）
}