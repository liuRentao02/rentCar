package com.suse.campus_rent.service.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.common.exception.BusinessException;
import com.suse.campus_rent.dto.app.ForgetUser;
import com.suse.campus_rent.dto.app.LoginUser;
import com.suse.campus_rent.dto.app.RegisterUser;
import com.suse.campus_rent.dto.app.UserUpdateDTO;
import com.suse.campus_rent.entity.*;
import com.suse.campus_rent.mapper.*;
import com.suse.campus_rent.util.FileUploadUtil;
import com.suse.campus_rent.vo.app.ProfileVO;
import com.suse.campus_rent.service.app.service.UserService;
import com.suse.campus_rent.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * UserServiceImpl
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/2 00:34
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final FileUploadUtil fileUtil;
    private final UserOperationLogMapper userOperationLogMapper;

    @Override
    public Result<?> login(LoginUser loginUser, HttpServletRequest request) {
        try {
            // 1. 认证
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginUser.getAccount(), loginUser.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);

            // 2. 获取自定义 UserDetails
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = customUserDetails.getUser(); // 直接从认证对象中获取 User 实体

            // 3. 生成 JWT（包含角色信息）
            String token = jwtUtil.generateTokenWithUserId(user.getId(), user.getUsername(), user.getRole());

            // 记录登录日志
            UserOperationLog log = new UserOperationLog();
            log.setUserId(user.getId());
            log.setUsername(user.getUsername());
            log.setAction("LOGIN");
            log.setContent("登录成功");
            log.setResult("SUCCESS");
            userOperationLogMapper.insert(log);

            // 4. 构建返回结果
            user.setPassword(null); // 隐藏密码
            user.setUpdateTime(null);
            user.setAvatar(user.getAvatar());
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            map.put("userInfo", user);
            map.put("role", authentication.getAuthorities());

            userMapper.update(new LambdaUpdateWrapper<User>()
                    .set(User::getLastLoginTime, LocalDateTime.now())
                    .eq(User::getId, user.getId()));

            return Result.success(map);
        } catch (BadCredentialsException e) {
            return Result.error("用户名或密码错误");
        } catch (DisabledException e) {
            return Result.error("账号已被禁用");
        } catch (AuthenticationException e) {
            return Result.error("登录失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> register(RegisterUser registerUser) {
        log.info("注册: {}", registerUser);

        // 1. 基础校验（密码必填）
        if (StringUtils.isBlank(registerUser.getPassword())) {
            return Result.badRequest("密码不能为空");
        } else if (StringUtils.isBlank(registerUser.getEmail()) && StringUtils.isBlank(registerUser.getPhone())) {
            return Result.badRequest("手机号或邮箱不能为空");
        }

        // 将空字符串转为 null，并 trim 掉前后空格
        String username = "用户" + LocalDate.now();
        String email = StringUtils.isBlank(registerUser.getEmail()) ? null : registerUser.getEmail().trim();
        String iphone = StringUtils.isBlank(registerUser.getPhone()) ? null : registerUser.getPhone().trim();
        String nickname = registerUser.getNickname(); // 后面处理

        // 2. 检查唯一性（只检查非空字段）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                .or().eq(email != null, "email", email)
                .or().eq(iphone != null, "iphone", iphone);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return Result.badRequest("用户名、邮箱或手机号已存在");
        }

        // 3. 创建用户实体
        User user = new User();
        user.setUsername(username);
        user.setIphone(iphone);
        user.setEmail(email);

        // 处理昵称：如果为空则生成默认昵称
        if (StringUtils.isBlank(nickname)) {
            user.setNickname("用户" + LocalDate.now()); // 示例默认昵称
        } else {
            user.setNickname(nickname.trim());
        }

        // 密码加密
        user.setPassword(passwordEncoder.encode(registerUser.getPassword()));
        user.setRole("user");
        user.setState(1);

        // 4. 保存到数据库
        int insert = userMapper.insert(user);
        if (insert > 0) {
            log.info("用户注册成功: {}", user.getUsername());
            user.setPassword(null);
            user.setUpdateTime(null);
            user.setCreateTime(null);
            return Result.success(user);
        } else {
            return Result.error("注册失败，请稍后重试");
        }
    }

    @Override
    public Result<?> getUserInfoById(Long id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            user.setPassword(null);
            user.setUpdateTime(null);
            user.setCreateTime(null);
            return Result.success(user);
        }
        return Result.error("用户不存在");
    }

    @Override
    public Result<?> getProfile(Long id) {
        ProfileVO user = userMapper.selectProfile(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.buildProfile();
        return Result.success(user);
    }

    @Override
    @Transactional
    public Result<?> updateProfile(UserUpdateDTO dto) {
        User user = userMapper.selectById(dto.getId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (dto.getAvatar() != null && !dto.getAvatar().isEmpty()) {
            if (dto.getAvatar().startsWith("data:image/")) {
                try {
                    user.setAvatar(fileUtil.uploadBase64Image(dto.getAvatar()));
                } catch (IOException e) {
                    throw new BusinessException("图片错误");
                }
            }
        }

        // 手机号唯一性校验（如果修改了且不同于原手机号）
        if (dto.getIphone() != null && !dto.getIphone().trim().isEmpty() && !dto.getIphone().equals(user.getIphone())) {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("iphone", dto.getIphone());
            if (userMapper.selectCount(wrapper) > 0) {
                throw new BusinessException("手机号已被其他用户使用");
            }
        }

        // 邮箱唯一性校验
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty() && !dto.getEmail().equals(user.getEmail())) {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("email", dto.getEmail());
            if (userMapper.selectCount(wrapper) > 0) {
                throw new BusinessException("邮箱已被其他用户使用");
            }
        }

        // 用户名唯一性校验（如果修改了且不同于原用户名）
        if (dto.getUsername() != null && !dto.getUsername().trim().isEmpty()
                && !dto.getUsername().equals(user.getUsername())) {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", dto.getUsername().trim());
            if (userMapper.selectCount(wrapper) > 0) {
                throw new BusinessException("用户名已被其他用户使用");
            }
        }

        // 更新用户名
        BeanUtils.copyProperties(dto, user, "id", "role", "password", "createTime", "avatar");

        int i = userMapper.updateById(user);
        if (i <= 0) {
            throw new BusinessException("更新失败");
        }
        return Result.success("更新成功");
    }

    @Override
    public Object Forget(ForgetUser user) {
        if (user == null) {
            throw new BusinessException("参数不能为空");
        }
        LambdaQueryWrapper<User> wq = new LambdaQueryWrapper<>();
        wq.eq(User::getIphone, user.getAccount())
                .or()
                .eq(User::getEmail, user.getAccount());
        List<User> users = userMapper.selectList(wq);

        List<Map<String, Object>> maps = new ArrayList<>();
        if (StringUtils.isBlank(user.getPassword())) {
            for (User user1 : users) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", user1.getId());
                map.put("avatar", user1.getAvatar());
                map.put("nickname", user1.getNickname());
                map.put("username", user1.getUsername());
                map.put("account", user1.getUsername());

                maps.add(map);
            }
            return maps;
        }

        LambdaUpdateWrapper<User> wu = new LambdaUpdateWrapper<>();
        wu.eq(User::getId, user.getId())
                .set(User::getPassword, passwordEncoder.encode(user.getPassword()));
        int update = userMapper.update(wu);
        return update == 1 ? "成功" : "失败";
    }

    @Override
    public Result<?> updatePassword(Long id, String oldPassword, String newPassword) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (oldPassword != null && !passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        // ✅ 正确
        if (newPassword == null || newPassword.isBlank()) {
            throw new BusinessException("新密码不能为空");
        }
        if (newPassword.equals(oldPassword)) {
            throw new BusinessException("新密码不能与旧密码相同");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        int i = userMapper.updateById(user);
        if (i <= 0) {
            return Result.error(400, "修改失败");
        }
        return Result.success();
    }
}
