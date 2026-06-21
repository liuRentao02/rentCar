package com.suse.campus_rent.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.suse.campus_rent.common.ListLongTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "conversation", autoResultMap = true) // 必须开启 autoResultMap
public class Conversation {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    private LocalDateTime time;

    /**
     * 参与用户ID列表，JSON格式，如 [101,102]
     */
    @TableField(typeHandler = ListLongTypeHandler.class)
    private List<Long> userId;
}