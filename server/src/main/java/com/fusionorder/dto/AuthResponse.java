package com.fusionorder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 认证响应数据传输对象
 * 用于返回用户登录后的认证信息，包含JWT Token和用户基本信息
 * 
 * @author FusionOrder Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    /**
     * JWT Token，用于后续请求的身份验证
     */
    private String token;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 用户角色
     */
    private String role;
}

