package com.fusionorder.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 认证请求数据传输对象
 * 用于接收用户登录请求的用户名和密码
 * 
 * @author FusionOrder Team
 */
@Data
public class AuthRequest {
    
    /**
     * 用户名，不能为空
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    /**
     * 密码，不能为空
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}

