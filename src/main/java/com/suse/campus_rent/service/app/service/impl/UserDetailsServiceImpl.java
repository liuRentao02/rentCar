package com.suse.campus_rent.service.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.suse.campus_rent.entity.User;
import com.suse.campus_rent.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserDetailsServiceImpl
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/2 14:26
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                .or(queryWrapper1 -> queryWrapper1.eq("email", username))
                .or(queryWrapper1 -> queryWrapper1.eq("iphone", username));
        List<User> users = userMapper.selectList(queryWrapper);
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if (users.size() > 1) {
            // 理论上不应出现多个，但若出现需处理
            log.warn("存在多个用户：{}", username);
        }

        User user = users.get(0);
        log.info("用户: {}", user);

        String role = user.getRole();
        List<GrantedAuthority> authorities;
        if (role == null || role.trim().isEmpty()) {
            authorities = AuthorityUtils.createAuthorityList("user");
        } else {
            authorities = AuthorityUtils.createAuthorityList(role);
        }
        return new CustomUserDetails(user, authorities);
    }
}
