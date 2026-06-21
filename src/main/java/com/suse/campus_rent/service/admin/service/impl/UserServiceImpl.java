package com.suse.campus_rent.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.dto.admin.UserCreateDTO;
import com.suse.campus_rent.dto.admin.UserQueryDTO;
import com.suse.campus_rent.dto.admin.UserUpdateDTO;
import com.suse.campus_rent.entity.User;
import com.suse.campus_rent.mapper.UserMapper;
import com.suse.campus_rent.mapper.UserOperationLogMapper;
import com.suse.campus_rent.service.admin.service.UserService;
import com.suse.campus_rent.util.MyBeanUtils;
import com.suse.campus_rent.vo.admin.StatisticsVO;
import com.suse.campus_rent.vo.admin.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("adminUserService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserOperationLogMapper userOperationLogMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public StatisticsVO getStatistics() {
        long all = userMapper.countAll();
        long active = userMapper.countActive();      // state=1
        long inactive = userMapper.countInactive();  // state=0
        long admin = userMapper.countAdmin();        // role='admin'
        return new StatisticsVO(all, active, inactive, admin);
    }

    @Override
    public IPage<UserVO> listUsers(UserQueryDTO queryDTO) {
        // 构建分页对象
        Page<User> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        // 关键词搜索：用户名、邮箱、手机号（iphone）
        if (StringUtils.hasText(queryDTO.getKeyword())) {
            wrapper.and(w -> w.like(User::getUsername, queryDTO.getKeyword())
                    .or().like(User::getEmail, queryDTO.getKeyword())
                    .or().like(User::getIphone, queryDTO.getKeyword())
                    .or().like(User::getNickname, queryDTO.getKeyword()));
        }
        // 角色筛选
        if (StringUtils.hasText(queryDTO.getRole())) {
            wrapper.eq(User::getRole, queryDTO.getRole());
        }
        // 状态筛选（state：0-禁用，1-正常）
        if (queryDTO.getState() != null) {
            wrapper.eq(User::getState, queryDTO.getState());
        }
        // 注册日期筛选：查询当天00:00到23:59
        if (queryDTO.getRegisterDate() != null) {
            LocalDate date = queryDTO.getRegisterDate();
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(LocalTime.MAX);
            wrapper.between(User::getCreateTime, start, end);
        }
        // 默认按创建时间倒序
        wrapper.orderByDesc(User::getCreateTime);

        // 执行分页查询
        Page<User> userPage = userMapper.selectPage(page, wrapper);

        // 转换为VO
        IPage<UserVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        voPage.setRecords(userPage.getRecords().stream()
                .map(this::convertToUserVO)
                .collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public UserVO getUserDetail(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToUserDetailVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(UserCreateDTO createDTO) {
        // 检查唯一性
        checkUnique(createDTO.getPhone(), createDTO.getEmail(), null);
        User user = new User();
        MyBeanUtils.copyPropertiesIgnoreNullAndEmpty(createDTO, user);
        user.setPassword(passwordEncoder.encode("123456"));
        userMapper.insert(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateDTO updateDTO) {
        User existing = userMapper.selectById(updateDTO.getId());
        if (existing == null) {
            throw new BusinessException("用户不存在");
        }

        // 唯一性校验：如果手机号或邮箱被修改，检查新值是否被其他用户占用
        String newPhone = updateDTO.getPhone();
        String newEmail = updateDTO.getEmail();
        if ((newPhone != null && !newPhone.equals(existing.getIphone())) ||
                (newEmail != null && !newEmail.equals(existing.getEmail()))) {
            checkUnique(newPhone, newEmail, updateDTO.getId());
        }

        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", updateDTO.getId());

        // 字段更新（与之前相同）
        if (StringUtils.hasText(updateDTO.getRealName())) {
            wrapper.set("real_name", updateDTO.getRealName());
        }
        if (StringUtils.hasText(updateDTO.getNickname())) {
            wrapper.set("nickname", updateDTO.getNickname());
        }
        if (StringUtils.hasText(updateDTO.getUsername())) {
            wrapper.set("username", updateDTO.getUsername());
        }
        if (StringUtils.hasText(updateDTO.getGender())) {
            wrapper.set("gender", updateDTO.getGender());
        }
        if (StringUtils.hasText(updateDTO.getPhone())) {
            wrapper.set("iphone", updateDTO.getPhone());
        }
        if (StringUtils.hasText(updateDTO.getEmail())) {
            wrapper.set("email", updateDTO.getEmail());
        }
        if (StringUtils.hasText(updateDTO.getRole())) {
            wrapper.set("role", updateDTO.getRole());
        }
        if (StringUtils.hasText(updateDTO.getIdCard())) {
            wrapper.set("id_card", updateDTO.getIdCard());
        }
        if (StringUtils.hasText(updateDTO.getDrivingLicense())) {
            wrapper.set("driving_license", updateDTO.getDrivingLicense());
        }
        if (updateDTO.getState() != null) {
            wrapper.set("state", updateDTO.getState());
        }
        if (StringUtils.hasText(updateDTO.getAddress())) {
            wrapper.set("address", updateDTO.getAddress());
        }
        if (updateDTO.getPoints() != null) {
            wrapper.set("points", updateDTO.getPoints());
        }
        if (StringUtils.hasText(updateDTO.getRemark())) {
            log.info("数据库没有备注字段，忽略：{}", updateDTO.getRemark());
        }

        userMapper.update(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        log.info("尝试删除用户，ID: {}", id);
        int rows = userMapper.deleteById(id);
        log.info("删除用户结果，影响行数: {}", rows);
        if (rows == 0) {
            throw new BusinessException("删除失败，请稍后重试");
        }
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 检查手机号、邮箱是否唯一（排除自身）
     */
    /**
     * 检查手机号和邮箱是否唯一（排除自身）
     */
    private void checkUnique(String phone, String email, Long excludeId) {
        if (phone == null && email == null) return;

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> {
            if (phone != null) {
                w.eq(User::getIphone, phone);
            }
            if (email != null) {
                if (phone != null) {
                    w.or().eq(User::getEmail, email);
                } else {
                    w.eq(User::getEmail, email);
                }
            }
        });
        if (excludeId != null) {
            wrapper.ne(User::getId, excludeId);
        }
        Long count = userMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException("手机号或邮箱已被其他用户使用");
        }
    }

    /**
     * 将User实体转换为UserVO
     */
    private UserVO convertToUserVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setRealName(user.getRealName());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getIphone());
        vo.setRole(user.getRole());
        vo.setRentalCount(user.getTotalRentals());
        // 状态转换：state 1->active, 0->inactive
        vo.setStatus(user.getState());
        vo.setRegisterTime(user.getCreateTime());
        vo.setLastLogin(user.getLastLoginTime());
        vo.setIdCard(user.getIdCard());
        vo.setLicenseNo(user.getDrivingLicense());
        vo.setGender(user.getGender());
        vo.setAddress(user.getAddress());
        vo.setTotalSpent(user.getTotalSpent());
        vo.setBalance(user.getBalance());
        vo.setPoints(user.getPoints());
        // avatarColor 由前端生成，后端不返回
        log.info("userVo: {}", vo);
        return vo;
    }

    /**
     * 转换为详情VO（复用UserVO）
     */
    private UserVO convertToUserDetailVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(convertToUserVO(user), vo);
        return vo;
    }
}