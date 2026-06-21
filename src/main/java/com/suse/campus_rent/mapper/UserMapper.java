package com.suse.campus_rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suse.campus_rent.entity.User;
import com.suse.campus_rent.vo.app.ProfileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * UserMapper
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/2 00:22
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 统计所有未删除的用户总数
    @Select("SELECT COUNT(*) FROM user WHERE is_deleted = 0")
    long countAll();

    // 统计正常用户数（state=0）
    @Select("SELECT COUNT(*) FROM user WHERE is_deleted = 0 AND state = 1")
    long countActive();

    // 统计已禁用用户数（state=1）
    @Select("SELECT COUNT(*) FROM user WHERE is_deleted = 0 AND state = 0")
    long countInactive();

    // 统计管理员数
    @Select("SELECT COUNT(*) FROM user WHERE is_deleted = 0 AND role = 'admin'")
    long countAdmin();

    // 统计今日新增用户数
    @Select("SELECT COUNT(*) FROM user WHERE is_deleted = 0 AND DATE(create_time) = CURDATE()")
    Integer selectTodayNewUsers();

    List<Map<String, Object>> selectLatestUsersWithCert(@Param("limit") int limit);

    ProfileVO selectProfile(@Param("id") Long id);
}
