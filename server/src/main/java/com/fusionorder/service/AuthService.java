package com.fusionorder.service;

import com.fusionorder.dto.AuthRequest;
import com.fusionorder.dto.AuthResponse;
import com.fusionorder.entity.User;
import com.fusionorder.exception.BusinessException;
import com.fusionorder.repository.UserRepository;
import com.fusionorder.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务类
 * 负责用户登录认证，验证用户名密码并生成JWT Token
 * 
 * @author FusionOrder Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    
    /**
     * 用户数据访问层
     */
    private final UserRepository userRepository;
    
    /**
     * 密码编码器，用于验证密码
     */
    private final PasswordEncoder passwordEncoder;
    
    /**
     * JWT工具类，用于生成Token
     */
    private final JwtUtil jwtUtil;

    /**
     * 用户登录
     * 验证用户名和密码，如果验证通过则生成JWT Token返回
     * 
     * @param request 登录请求（包含用户名和密码）
     * @return 认证响应（包含Token和用户信息）
     * @throws BusinessException 用户名或密码错误、账户被禁用时抛出
     */
    public AuthResponse login(AuthRequest request) {
        log.info("用户登录尝试, username: {}", request.getUsername());
        
        // 查找用户
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("登录失败：用户不存在, username: {}", request.getUsername());
                    return new BusinessException("用户名或密码错误");
                });
        
        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("登录失败：密码错误, username: {}", request.getUsername());
            throw new BusinessException("用户名或密码错误");
        }
        
        // 检查账户是否启用
        if (!user.getEnabled()) {
            log.warn("登录失败：账户已被禁用, username: {}", request.getUsername());
            throw new BusinessException("账户已被禁用");
        }
        
        // 生成JWT Token
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        log.info("用户登录成功, username: {}, role: {}", user.getUsername(), user.getRole());
        
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }
}

