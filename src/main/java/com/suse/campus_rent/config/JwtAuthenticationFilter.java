package com.suse.campus_rent.config;

import com.suse.campus_rent.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 认证过滤器，用于验证每个请求中的 JWT token。
 * 如果 token 有效，则将用户信息设置到 SecurityContext 中。
 *
 * @author LiuRentao
 * @since 2026/2/11
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. 获取 Authorization 头
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 2. 如果没有 token 或格式不正确，直接放行（交给后续过滤器或认证入口）
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 提取 JWT
        jwt = authHeader.substring(7);

        try {
            // 4. 从 token 中解析用户名
            username = jwtUtil.extractUsername(jwt);
        } catch (ExpiredJwtException e) {
            log.warn("JWT token 已过期: {}", e.getMessage());
            sendErrorResponse(response, "Token expired");
            return;
        } catch (SignatureException e) {
            log.warn("JWT 签名无效: {}", e.getMessage());
            sendErrorResponse(response, "Invalid token signature");
            return;
        } catch (Exception e) {
            log.warn("JWT 解析失败: {}", e.getMessage());
            sendErrorResponse(response, "Invalid token");
            return;
        }
        UserDetails userDetails;
        // 5. 如果用户名不为空，且当前 SecurityContext 中没有认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 6. 加载用户详情
            userDetails = this.userDetailsService.loadUserByUsername(username);

            // 7. 验证 token 是否有效（包括用户名校验和过期检查）
            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                // 8. 创建认证对象并设置到 SecurityContext
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("成功设置认证信息: {}", username);
            } else {
                log.warn("JWT token 无效（可能用户不匹配或已过期）: {}", username);
                sendErrorResponse(response, "Invalid token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 发送统一的错误响应
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("{\"code\":%d,\"message\":\"%s\"}", HttpServletResponse.SC_UNAUTHORIZED, message));
    }
}