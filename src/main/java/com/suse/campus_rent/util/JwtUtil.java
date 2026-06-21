package com.suse.campus_rent.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * 获取用于签名的密钥
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = extractAllClaims(token);
        Object userIdObj = claims.get("userId");
        if (userIdObj instanceof Number) {
            return ((Number) userIdObj).longValue();
        } else if (userIdObj instanceof String) {
            return Long.parseLong((String) userIdObj);
        }
        return null;
    }

    /**
     * 生成包含 userId 和角色信息的 Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param role     角色
     */
    public String generateTokenWithUserId(Long userId, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        return createToken(claims, username);
    }

    // 生成 Token
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // 生成带自定义claims的Token
    public String generateToken(Map<String, Object> claims, String username) {
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)              // 0.12.3 使用 claims() 替代 setClaims()
                .subject(subject)            // 0.12.3 使用 subject() 替代 setSubject()
                .issuedAt(new Date())        // 0.12.3 使用 issuedAt() 替代 setIssuedAt()
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // 0.12.3 使用 expiration() 替代 setExpiration()
                .signWith(getSigningKey())   // 0.12.3 使用 signWith(SecretKey) 替代 signWith(SignatureAlgorithm, String)
                .compact();
    }

    // 从 Token 中获取用户名
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 检查 Token 是否有效
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // 检查 Token 是否过期
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 从 Token 中获取过期时间
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 从 Token 中获取指定信息
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 解析 Token - 0.12.3 版本的新API
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())  // 0.12.3 使用 verifyWith() 替代 setSigningKey()
                .build()
                .parseSignedClaims(token)     // 0.12.3 使用 parseSignedClaims() 替代 parseClaimsJws()
                .getPayload();                // 0.12.3 使用 getPayload() 替代 getBody()
    }

    /**
     * 从Token中获取所有Claims（带异常处理的版本）
     */
    public Claims getAllClaimsFromToken(String token) {
        try {
            return extractAllClaims(token);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证Token是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取Token剩余有效时间（毫秒）
     */
    public long getRemainingTime(String token) {
        Date expiration = extractExpiration(token);
        return expiration.getTime() - System.currentTimeMillis();
    }

    /**
     * 刷新Token
     */
    public String refreshToken(String token) {
        Claims claims = extractAllClaims(token);
        return Jwts.builder()
                .claims(claims)
                .subject(claims.getSubject())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 获取Token的签发时间
     */
    public Date getIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    // 添加以下方法到 JwtUtil 类中

    /**
     * 生成包含角色信息的Token
     *
     * @param username 用户名
     * @param role     用户角色
     * @return JWT token
     */
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }
}