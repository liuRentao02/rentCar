package com.suse.campus_rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suse.campus_rent.entity.UserOperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface UserOperationLogMapper extends BaseMapper<UserOperationLog> {

    @Select("SELECT COUNT(*) FROM user_operation_log WHERE user_id = #{userId} AND action = 'LOGIN' AND result = 'SUCCESS'")
    Integer countLogin(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM user_operation_log WHERE user_id = #{userId} AND create_time >= #{startTime}")
    Integer countUserOperations(@Param("userId") Long userId, @Param("startTime") LocalDateTime startTime);
}