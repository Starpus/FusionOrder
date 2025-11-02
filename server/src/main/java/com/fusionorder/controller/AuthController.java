package com.fusionorder.controller;

import com.fusionorder.dto.ApiResponse;
import com.fusionorder.dto.AuthRequest;
import com.fusionorder.dto.AuthResponse;
import com.fusionorder.dto.UserDTO;
import com.fusionorder.entity.User;
import com.fusionorder.service.AuthService;
import com.fusionorder.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 提供用户注册和登录功能
 * 
 * @author FusionOrder Team
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户注册和登录接口")
public class AuthController {
    
    /**
     * 认证服务
     */
    private final AuthService authService;
    
    /**
     * 用户服务
     */
    private final UserService userService;

    /**
     * 用户注册
     * 允许所有用户访问，创建新用户账号
     * 
     * @param user 用户注册信息
     * @return 注册成功的用户信息
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "创建新用户账号，用户名和邮箱必须唯一")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "注册成功", 
                content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "参数验证失败或用户名/邮箱已存在")
    })
    public ResponseEntity<ApiResponse<UserDTO>> register(
            @Parameter(description = "用户注册信息，包含用户名、密码、邮箱等") 
            @Valid @RequestBody User user) {
        log.info("用户注册, username: {}", user.getUsername());
        UserDTO userDTO = userService.register(user);
        log.info("用户注册成功, userId: {}, username: {}", userDTO.getId(), userDTO.getUsername());
        return ResponseEntity.ok(ApiResponse.success("注册成功", userDTO));
    }

    /**
     * 用户登录
     * 验证用户名密码，返回JWT Token
     * 
     * @param request 登录请求（用户名和密码）
     * @return 认证响应（包含Token和用户信息）
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "验证用户名和密码，返回JWT Token用于后续接口认证")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "登录成功", 
                content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "用户名或密码错误，或账户已被禁用")
    })
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Parameter(description = "登录请求，包含用户名和密码") 
            @Valid @RequestBody AuthRequest request) {
        log.info("用户登录, username: {}", request.getUsername());
        AuthResponse response = authService.login(request);
        log.info("用户登录成功, username: {}, role: {}", response.getUsername(), response.getRole());
        return ResponseEntity.ok(ApiResponse.success("登录成功", response));
    }
}

