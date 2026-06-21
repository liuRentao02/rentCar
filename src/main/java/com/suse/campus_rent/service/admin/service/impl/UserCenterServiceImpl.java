package com.suse.campus_rent.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.dto.admin.ChangePasswordDTO;
import com.suse.campus_rent.dto.admin.ProfileUpdateDTO;
import com.suse.campus_rent.entity.User;
import com.suse.campus_rent.entity.UserOperationLog;
import com.suse.campus_rent.entity.Order;
import com.suse.campus_rent.mapper.OrderMapper;
import com.suse.campus_rent.mapper.UserMapper;
import com.suse.campus_rent.mapper.UserOperationLogMapper;
import com.suse.campus_rent.service.admin.service.UserCenterService;
import com.suse.campus_rent.util.FileUploadUtil;
import com.suse.campus_rent.vo.admin.UserProfileVO;
import com.suse.campus_rent.vo.admin.UserStatsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service("admin UserCenterService")
@RequiredArgsConstructor
public class UserCenterServiceImpl implements UserCenterService {

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final UserOperationLogMapper operationLogMapper;
    private final PasswordEncoder passwordEncoder;
    private final FileUploadUtil fileUploadUtil;

    @Override
    public UserProfileVO getCurrentUserProfile(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserProfileVO vo = new UserProfileVO();
        BeanUtils.copyProperties(user, vo);
        vo.setPhone(user.getIphone());

        // 从日志表获取最近一次登录IP和时间
        vo.setLastLoginIp("未知");

        // 最近登录时间也需要从日志获取
        LambdaQueryWrapper<UserOperationLog> logWrapper = new LambdaQueryWrapper<>();
        logWrapper.eq(UserOperationLog::getUserId, userId)
                .eq(UserOperationLog::getAction, "LOGIN")
                .orderByDesc(UserOperationLog::getCreateTime)
                .last("LIMIT 1");
        UserOperationLog lastLoginLog = operationLogMapper.selectOne(logWrapper);
        vo.setLastLoginTime(lastLoginLog != null ? lastLoginLog.getCreateTime() : user.getCreateTime());

        // 角色名称
        vo.setRole(getRoleName(user.getRole()));
        return vo;
    }

    private String getRoleName(String roleCode) {
        return switch (roleCode) {
            case "admin" -> "超级管理员";
            case "operator" -> "运营人员";
            case "user" -> "普通用户";
            default -> roleCode;
        };
    }

    @Override
    public UserStatsVO getUserStats(Long userId) {
        UserStatsVO vo = new UserStatsVO();

        // 本月操作数（假设 countUserOperations 返回 Integer，没问题）
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        vo.setMonthlyOperations(operationLogMapper.countUserOperations(userId, startOfMonth));

        // 总登录次数（假设 countLogin 返回 Integer）
        vo.setLoginCount(operationLogMapper.countLogin(userId));

        // 创建订单数：selectCount 返回 Long，需转换为 int
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getUserId, userId);
        Long count = orderMapper.selectCount(orderWrapper);
        vo.setCreatedOrders(count != null ? count.intValue() : 0);

        // 处理工单数（设为0）
        vo.setProcessedTickets(0);

        return vo;
    }

    @Override
    @Transactional
    public void updateProfile(Long userId, ProfileUpdateDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException("用户不存在");

        // 检查手机号唯一性
        if (dto.getPhone() != null && !dto.getPhone().equals(user.getIphone())) {
            LambdaQueryWrapper<User> phoneWrapper = new LambdaQueryWrapper<>();
            phoneWrapper.eq(User::getIphone, dto.getPhone());
            if (userMapper.selectCount(phoneWrapper) > 0) {
                throw new BusinessException("手机号已被其他用户使用");
            }
        }
        // 检查邮箱唯一性
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            LambdaQueryWrapper<User> emailWrapper = new LambdaQueryWrapper<>();
            emailWrapper.eq(User::getEmail, dto.getEmail());
            if (userMapper.selectCount(emailWrapper) > 0) {
                throw new BusinessException("邮箱已被其他用户使用");
            }
        }

        // 更新字段
        if (dto.getNickname() != null) user.setNickname(dto.getNickname());
        if (dto.getRealName() != null) user.setRealName(dto.getRealName());
        if (dto.getPhone() != null) user.setIphone(dto.getPhone());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getGender() != null) user.setGender(dto.getGender());
        if (dto.getAddress() != null) user.setAddress(dto.getAddress());
        if (dto.getIdCard() != null) user.setIdCard(dto.getIdCard());

        userMapper.updateById(user);

        // 记录操作日志
        UserOperationLog log = new UserOperationLog();
        log.setUserId(userId);
        log.setUsername(user.getUsername());
        log.setAction("UPDATE_PROFILE");
        log.setContent("更新了个人资料");
        log.setResult("SUCCESS");
        operationLogMapper.insert(log);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException("用户不存在");

        // 验证旧密码
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }
        // 设置新密码
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userMapper.updateById(user);

        // 记录日志
        UserOperationLog log = new UserOperationLog();
        log.setUserId(userId);
        log.setUsername(user.getUsername());
        log.setAction("CHANGE_PASSWORD");
        log.setContent("修改了登录密码");
        log.setResult("SUCCESS");
        operationLogMapper.insert(log);
    }

    @Override
    public String uploadAvatar(Long userId, MultipartFile file) throws IOException {
        User user = userMapper.selectById(userId);
        if (user == null) throw new BusinessException("用户不存在");

        String avatarUrl = fileUploadUtil.uploadImage(file);
        user.setAvatar(avatarUrl);
        userMapper.updateById(user);

        UserOperationLog log = new UserOperationLog();
        log.setUserId(userId);
        log.setUsername(user.getUsername());
        log.setAction("UPLOAD_AVATAR");
        log.setContent("更新了头像");
        log.setResult("SUCCESS");
        operationLogMapper.insert(log);
        return avatarUrl;
    }
}