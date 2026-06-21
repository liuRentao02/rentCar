package com.suse.campus_rent.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.suse.campus_rent.entity.User;
import com.suse.campus_rent.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleExpireScheduler {

    private final UserMapper userMapper;

    // 每天凌晨2点执行
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void checkExpiredRoles() {
        log.info("开始检查过期角色...");
        LocalDate today = LocalDate.now();

        // 扫描过期的学生
        List<User> expiredStudents = userMapper.selectList(new LambdaQueryWrapper<User>()
                .eq(User::getRole, "student")
                .isNotNull(User::getStudentExpireTime)
                .lt(User::getStudentExpireTime, today));
        for (User user : expiredStudents) {
            user.setRole("user");
            user.setStudentExpireTime(null);
            userMapper.updateById(user);
            log.info("学生过期用户ID: {}, 用户名: {}", user.getId(), user.getUsername());
        }
        log.info("过期角色检查完成。");
    }
}