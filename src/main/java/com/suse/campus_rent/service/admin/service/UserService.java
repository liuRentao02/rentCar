package com.suse.campus_rent.service.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suse.campus_rent.dto.admin.UserCreateDTO;
import com.suse.campus_rent.dto.admin.UserQueryDTO;
import com.suse.campus_rent.dto.admin.UserUpdateDTO;
import com.suse.campus_rent.entity.UserOperationLog;
import com.suse.campus_rent.vo.admin.StatisticsVO;
import com.suse.campus_rent.vo.admin.UserVO;

import java.util.List;

public interface UserService {
    /**
     * 获取用户统计数据
     */
    StatisticsVO getStatistics();

    /**
     * 分页查询用户列表
     */
    IPage<UserVO> listUsers(UserQueryDTO queryDTO);

    /**
     * 根据ID获取用户详情
     */
    UserVO getUserDetail(Long id);

    /**
     * 新增用户
     */
    void createUser(UserCreateDTO createDTO);

    /**
     * 更新用户
     */
    void updateUser(UserUpdateDTO updateDTO);

    /**
     * 删除用户（逻辑删除）
     */
    void deleteUser(Long id);
}