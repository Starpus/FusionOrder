package com.fusionorder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 存储用户的基本信息，包括用户名、密码、邮箱、电话、角色等
 * 
 * @author FusionOrder Team
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    /**
     * 用户ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 用户名，唯一，不能为空，最大长度50
     */
    @Column(unique = true, nullable = false, length = 50)
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50之间")
    private String username;
    
    /**
     * 密码，不能为空，存储加密后的密码
     */
    @Column(nullable = false)
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度至少6位")
    private String password;
    
    /**
     * 邮箱，唯一，可为空
     */
    @Column(unique = true, length = 100)
    @Email(message = "邮箱格式不正确")
    private String email;
    
    /**
     * 电话号码
     */
    @Column(length = 20)
    private String phone;
    
    /**
     * 用户角色，枚举类型，默认USER
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;
    
    /**
     * 账户是否启用，默认true
     */
    @Column(nullable = false)
    private Boolean enabled = true;
    
    /**
     * 创建时间，自动设置
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间，自动更新
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * 保存前自动设置创建时间和更新时间
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * 更新前自动设置更新时间
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * 用户角色枚举
     */
    public enum Role {
        /**
         * 普通用户
         */
        USER,
        
        /**
         * 管理员
         */
        ADMIN,
        
        /**
         * 产品管理员
         */
        MANAGER
    }
}

