package com.suse.campus_rent.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.suse.campus_rent.common.interfaces.OperLog;
import com.suse.campus_rent.common.Result;
import com.suse.campus_rent.dto.app.ForgetUser;
import com.suse.campus_rent.dto.app.LoginUser;
import com.suse.campus_rent.dto.app.RegisterUser;
import com.suse.campus_rent.dto.app.TokenValidateRequest;
import com.suse.campus_rent.entity.User;
import com.suse.campus_rent.mapper.UserMapper;
import com.suse.campus_rent.service.app.service.UserService;
import com.suse.campus_rent.util.JwtUtil;
import com.suse.campus_rent.util.VerifyCodeUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController
 *
 * @author LiuRentao
 * @version 1.0
 * @since 2026/3/2 00:35
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;  // 注入 JwtUtil
    private final UserMapper userMapper;

    @GetMapping("/captcha")
    public Result<?> code(@RequestParam("w") int w, @RequestParam("h") int h, @RequestParam("s") Integer size) {
        return Result.success(VerifyCodeUtils.generate(w, h, size));
    }

    @OperLog(title = "用户登录", category = "AUTH", level = "INFO")
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginUser loginUser, HttpServletRequest request) {
        log.info("登录: {}", loginUser);
        return userService.login(loginUser, request);
    }

    @OperLog(title = "用户注册", category = "AUTH")
    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterUser registerUser) {
        log.info("注册: {}", registerUser);
        return userService.register(registerUser);
    }

    /**
     * 验证 Token 有效性，并与传入的用户名、角色比对
     *
     * @param request 包含 token、username、role 的请求体
     * @return 验证结果
     */
    @PostMapping("/validate")
    public Result<?> validateToken(@RequestBody TokenValidateRequest request) {
        log.info("验证Token: {}", request);

        String token = request.getToken();
        String username = request.getUsername();
        String role = request.getRole();

        // 1. 验证 token 是否有效（签名正确且未过期）
        if (!jwtUtil.validateToken(token)) {
            return Result.error("无效的token或token已过期");
        }

        // 2. 从 token 中提取用户名
        String extractedUsername = jwtUtil.extractUsername(token);
        if (!username.equals(extractedUsername)) {
            return Result.error("用户信息不匹配");
        }

        // 3. 如果需要验证角色，从 token 的 claims 中提取（假设生成 token 时已存入角色）
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        log.info("从token中提取的角色：{}", claims);
        if (claims != null && claims.containsKey("role")) {
            String extractedRole = claims.get("role", String.class);
            if (!role.equals(extractedRole)) {
                return Result.error("角色信息不匹配");
            }
        } else {
            // 如果 token 中不包含角色，可在此查询数据库进行验证（可选）
            // 例如：通过用户名从数据库获取角色并比对
            log.warn("Token中未包含角色信息，数据库验证");
            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("username", username);
            qw.eq("role", role);
            if (userMapper.selectCount(qw) == 0) {
                return Result.error("用户信息不匹配");
            }
        }

        return Result.success("验证通过");
    }

    @OperLog(title = "忘记密码", category = "AUTH", level = "WARN")
    @PostMapping("/forget")
    public Result<?> forget(@RequestBody ForgetUser user) {
        return Result.success(userService.Forget(user));
    }
}
