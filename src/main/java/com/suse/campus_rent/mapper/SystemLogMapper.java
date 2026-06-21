package com.suse.campus_rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suse.campus_rent.entity.SystemLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * SystemLogMapper
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/4/2 10:59
 */
@Mapper
public interface SystemLogMapper extends BaseMapper<SystemLog> {

    Map<String, Object> getStatistics();

}
