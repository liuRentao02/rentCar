package com.suse.campus_rent.common.interfaces;


import com.suse.campus_rent.entity.SystemLog;
import com.suse.campus_rent.entity.User;
import com.suse.campus_rent.mapper.SystemLogMapper;
import com.suse.campus_rent.service.app.service.impl.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 系统日志切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SystemLogAspect {

    private final SystemLogMapper systemLogMapper;

    // 创建一个单线程的线程池，专门用来异步写日志，不要阻塞主业务
    private final ExecutorService executorService = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1000),
            r -> new Thread(r, "log-save-thread")
    );

    /**
     * 环绕通知：在接口执行前后进行拦截
     */
    @Around("@annotation(mylog)")
    public Object around(ProceedingJoinPoint point, OperLog mylog) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 1. 提前组装好日志对象（主线程中获取，避免线程上下文丢失）
        SystemLog logEntity = new SystemLog();
        logEntity.setLevel(mylog.level());
        logEntity.setCategory(mylog.category());
        logEntity.setContent(mylog.title());

        // 2. 获取当前登录用户 (结合你的 Spring Security)
        try {
            // 修改后
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails customUser) {

                User currentUser = customUser.getUser(); // 拿到完整的 User 对象

                logEntity.setUserId(currentUser.getId());
                logEntity.setUsername(currentUser.getUsername());

            } else {
                logEntity.setUserId(0L);
                logEntity.setUsername("系统/匿名");
            }

        } catch (Exception e) {
            logEntity.setUsername("获取用户失败");
        }

        Object result;
        try {
            // 3. 执行业务方法
            result = point.proceed();

            // 4. 业务执行成功，记录详情
            long time = System.currentTimeMillis() - startTime;
            logEntity.setDetail("执行成功，耗时: " + time + "ms");

        } catch (Throwable e) {
            // 5. 业务执行失败，记录异常信息
            logEntity.setLevel("ERROR");
            logEntity.setDetail("执行失败: " + e.getMessage());
            throw e; // 别忘了把异常抛出去，让全局异常处理器去捕获
        } finally {
            // 6. 将保存操作扔进线程池异步执行（极其重要！）
            executorService.submit(() -> {
                try {
                    systemLogMapper.insert(logEntity);
                } catch (Exception e) {
                    log.error("保存系统日志失败", e);
                }
            });
        }
        return result;
    }

}
