package com.fusionorder.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/**
 * JWT工具类
 * 提供JWT Token的生成、解析、验证等功能
 * 
 * @author FusionOrder Team
 */
@Component
public class JwtUtil {

    /**
     * JWT密钥，从配置文件中读取
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * JWT Token过期时间（毫秒），从配置文件中读取
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 获取签名密钥
     * 用于签名和验证JWT Token
     * 
     * @return SecretKey签名密钥
     */
    private SecretKey getSigningKey() {
        // 确保密钥长度至少为256位（32字节）
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            // 如果密钥太短，使用HS256算法推荐的密钥生成方式
            // 注意：生产环境应使用足够长的密钥
            return Jwts.SIG.HS256.key().build();
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成JWT Token
     * 
     * @param username 用户名
     * @param role 用户角色
     * @return JWT Token字符串
     */
    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(username)  // 设置主题（用户名）
                .claim("role", role)  // 添加自定义声明（角色）
                .issuedAt(now)  // 设置签发时间
                .expiration(expiryDate)  // 设置过期时间
                .signWith(getSigningKey())  // 使用密钥签名
                .compact();  // 生成最终的Token字符串
    }

    /**
     * 从Token中提取用户名
     * 
     * @param token JWT Token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 从Token中提取用户角色
     * 
     * @param token JWT Token
     * @return 用户角色
     */
    public String getRoleFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("role", String.class));
    }

    /**
     * 从Token中提取过期时间
     * 
     * @param token JWT Token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 从Token中提取指定的声明信息
     * 
     * @param token JWT Token
     * @param claimsResolver 声明解析函数
     * @param <T> 返回类型
     * @return 声明信息
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从Token中解析所有声明信息
     * 
     * @param token JWT Token
     * @return Claims声明对象
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())  // 使用密钥验证签名
                .build()
                .parseSignedClaims(token)  // 解析签名后的Token
                .getPayload();  // 获取有效载荷（Claims）
    }

    /**
     * 检查Token是否已过期
     * 
     * @param token JWT Token
     * @return true表示已过期，false表示未过期
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 验证Token是否有效
     * 检查Token中的用户名是否匹配，且Token未过期
     * 
     * @param token JWT Token
     * @param username 要验证的用户名
     * @return true表示Token有效，false表示Token无效
     */
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }
}

