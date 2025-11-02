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

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        
        // 跳过公开端点的JWT验证（注意context-path是/api，所以路径不包含/api前缀）
        if (path.startsWith("/auth/") || 
            path.startsWith("/products") || 
            path.startsWith("/orders") ||
            path.startsWith("/uploads/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            String token = getTokenFromRequest(request);

            if (token != null && !token.isEmpty()) {
                try {
                    String username = jwtUtil.getUsernameFromToken(token);
                    if (username != null && jwtUtil.validateToken(token, username)) {
                        String role = jwtUtil.getRoleFromToken(token);

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                        );

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e) {
                    // Token无效或过期，清除认证信息，继续处理请求
                    SecurityContextHolder.clearContext();
                }
            }
            
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 捕获所有异常，确保请求能够继续
            filterChain.doFilter(request, response);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

