package com.suse.campus_rent.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.dto.admin.LogQueryDTO;
import com.suse.campus_rent.entity.SystemLog;
import com.suse.campus_rent.entity.User;
import com.suse.campus_rent.mapper.SystemLogMapper;
import com.suse.campus_rent.mapper.UserMapper;
import com.suse.campus_rent.service.admin.service.SystemLogService;
import com.suse.campus_rent.vo.admin.SystemLogVO;
import lombok.RequiredArgsConstructor;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SystemLogServiceImpl
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/4/2 11:00
 */
@Service("adminSystemLogService")
@RequiredArgsConstructor
public class SystemLogServiceImpl implements SystemLogService {

    private final SystemLogMapper systemLogMapper;
    private final UserMapper userMapper;

    private final HashMap<Long, String> realNameMap = new HashMap<>();

    /*
     * 获取日志信息
     */
    public Page<SystemLogVO> getSystemLog(LogQueryDTO queryDTO) {
        Page<SystemLog> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        LambdaQueryWrapper<SystemLog> qw = new LambdaQueryWrapper<>();
        //构建查询条件

        // 1. 构建查询条件
        // 关键字模糊搜索（搜内容）
        if (StringUtils.isNotBlank(queryDTO.getKeyword())) {
            qw.like(SystemLog::getContent, queryDTO.getKeyword());
        }
        // 内容模糊搜索
        if (StringUtils.isNotBlank(queryDTO.getType())) {
            qw.like(SystemLog::getCategory, queryDTO.getType());
        }
        // 日志级别精确匹配
        if (StringUtils.isNotBlank(queryDTO.getLevel())) {
            qw.eq(SystemLog::getLevel, queryDTO.getLevel());
        }
        // 时间范围查询转换 (核心修改点)
        if (queryDTO.getStartTime() != null) {
            // 开始时间：设定为当天的 00:00:00
            qw.ge(SystemLog::getCreateTime, queryDTO.getStartTime().atStartOfDay());
        }
        if (queryDTO.getEndTime() != null) {
            // 结束时间：设定为当天的 23:59:59
            qw.le(SystemLog::getCreateTime, queryDTO.getEndTime().atTime(23, 59, 59));
        }

        qw.orderByDesc(SystemLog::getCreateTime);
        Page<SystemLog> systemLogPage = systemLogMapper.selectPage(page, qw);

        List<SystemLogVO> systemLogVOList = new ArrayList<>();
        for (SystemLog systemLog : systemLogPage.getRecords()) {
            systemLogVOList.add(toVO(systemLog));
        }

        Page<SystemLogVO> resultPage = new Page<>(systemLogPage.getCurrent(), systemLogPage.getSize(), systemLogPage.getTotal());
        resultPage.setRecords(systemLogVOList);
        realNameMap.clear();
        return resultPage;
    }

    private SystemLogVO toVO(SystemLog log) {
        SystemLogVO vo = new SystemLogVO();
        // 复制基础字段
        vo.setId(log.getId());
        vo.setLevel(log.getLevel());
        vo.setCategory(log.getCategory());
        vo.setContent(log.getContent());
        vo.setDetail(log.getDetail());
        vo.setCreateTime(log.getCreateTime());

        // 处理用户名：优先使用日志中保存的 username，若为空则查询用户表
        String username = log.getUsername();
        if (username == null && log.getUserId() != null) {
            User user = userMapper.selectById(log.getUserId());
            if (user != null) {
                username = user.getRealName();
            } else {
                username = "已删除用户";
            }
        }
        vo.setUsername(username != null ? username : "未知用户");

        if (realNameMap.containsKey(log.getId())) {
            vo.setRealName(realNameMap.get(log.getId()).split("#")[0]);
            vo.setAvatar(realNameMap.get(log.getId()).split("#")[1]);
        } else {
            User user = userMapper.selectById(log.getUserId());
            if (user != null) {
                vo.setAvatar(user.getAvatar());
                vo.setRealName(user.getRealName());
            } else {
                vo.setRealName("未知用户");
            }
        }

        return vo;
    }

    @Override
    public Map<String, Object> getStatistics() {
        return systemLogMapper.getStatistics();
    }
}
