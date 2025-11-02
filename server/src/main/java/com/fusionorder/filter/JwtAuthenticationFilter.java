package com.fusionorder.filter;

import com.fusionorder.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT认证过滤器
 * 在每个请求中验证JWT Token，并将用户信息设置到Spring Security上下文中
 * 
 * @author FusionOrder Team
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * JWT工具类，用于解析和验证Token
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 过滤器核心方法
     * 从请求头中提取JWT Token，验证后设置认证信息
     * 
     * @param request HTTP请求
     * @param response HTTP响应
     * @param filterChain 过滤器链
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        
        // 跳过公开端点的JWT验证（注意context-path是/api，所以路径不包含/api前缀）
        // Swagger相关路径也需要跳过验证
        if (path.startsWith("/auth/") || 
            path.startsWith("/products") || 
            path.startsWith("/orders") ||
            path.startsWith("/uploads/") ||
            path.startsWith("/swagger-ui") ||
            path.startsWith("/v3/api-docs") ||
            path.startsWith("/swagger-resources") ||
            path.equals("/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            // 从请求头中获取Token
            String token = getTokenFromRequest(request);

            // 如果存在Token，进行验证
            if (token != null && !token.isEmpty()) {
                try {
                    // 从Token中提取用户名
                    String username = jwtUtil.getUsernameFromToken(token);
                    
                    // 验证Token是否有效（未过期且用户名匹配）
                    if (username != null && jwtUtil.validateToken(token, username)) {
                        // 从Token中提取角色信息
                        String role = jwtUtil.getRoleFromToken(token);

                        // 创建认证对象，设置用户名和角色
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                username,
                                null,  // 不需要密码凭证
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                        );

                        // 将认证信息设置到Spring Security上下文
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e) {
                    // Token无效或过期，清除认证信息，继续处理请求
                    // 这样公开接口仍然可以访问
                    SecurityContextHolder.clearContext();
                }
            }
            
            // 继续执行过滤器链
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 捕获所有异常，确保请求能够继续处理
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 从HTTP请求头中提取JWT Token
     * Token格式: "Bearer {token}"
     * 
     * @param request HTTP请求对象
     * @return JWT Token字符串，如果不存在则返回null
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // 移除"Bearer "前缀，返回实际的Token
            return bearerToken.substring(7);
        }
        return null;
    }
}

