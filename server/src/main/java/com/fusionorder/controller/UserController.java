package com.fusionorder.controller;

import com.fusionorder.dto.ApiResponse;
import com.fusionorder.dto.UserDTO;
import com.fusionorder.entity.User;
import com.fusionorder.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 * 提供用户相关的增删改查接口，仅管理员可访问
 * 
 * @author FusionOrder Team
 */
@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户相关的增删改查接口，仅管理员可访问")
public class UserController {
    
    /**
     * 用户服务
     */
    private final UserService userService;

    /**
     * 获取所有用户列表
     * 仅管理员可访问
     * 
     * @return 用户列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "获取所有用户", description = "获取系统中所有用户列表，仅管理员可访问")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查询成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "权限不足")
    })
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        log.info("管理员查询所有用户");
        List<UserDTO> users = userService.getAllUsers();
        log.info("查询到 {} 个用户", users.size());
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    /**
     * 根据ID获取用户信息
     * 仅管理员可访问
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "获取用户详情", description = "根据用户ID获取详细信息，仅管理员可访问")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查询成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "用户不存在"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "权限不足")
    })
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        log.info("管理员查询用户, userId: {}", id);
        UserDTO user = userService.getUserById(id);
        log.info("查询用户成功, userId: {}, username: {}", id, user.getUsername());
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    /**
     * 更新用户信息
     * 仅管理员可访问
     * 
     * @param id 用户ID
     * @param user 用户信息
     * @return 更新后的用户信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新用户信息", description = "更新用户信息，仅管理员可访问")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "更新成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "用户不存在"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "权限不足")
    })
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "用户信息") @Valid @RequestBody User user) {
        log.info("管理员更新用户, userId: {}", id);
        UserDTO updatedUser = userService.updateUser(id, user);
        log.info("更新用户成功, userId: {}, username: {}", id, updatedUser.getUsername());
        return ResponseEntity.ok(ApiResponse.success("用户更新成功", updatedUser));
    }

    /**
     * 删除用户
     * 仅管理员可访问
     * 
     * @param id 用户ID
     * @return 成功响应
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除用户", description = "删除用户，仅管理员可访问")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "删除成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "用户不存在"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "权限不足")
    })
    public ResponseEntity<ApiResponse<Object>> deleteUser(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        log.info("管理员删除用户, userId: {}", id);
        userService.deleteUser(id);
        log.info("删除用户成功, userId: {}", id);
        return ResponseEntity.ok(ApiResponse.success("用户删除成功"));
    }
}

