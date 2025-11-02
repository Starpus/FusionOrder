package com.fusionorder.dto;

import com.fusionorder.entity.User;
import lombok.Data;

/**
 * 用户数据传输对象
 * 用于在前端和后端之间传输用户信息，不包含敏感信息（如密码）
 * 
 * @author FusionOrder Team
 */
@Data
public class UserDTO {
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 电话号码
     */
    private String phone;
    
    /**
     * 用户角色
     */
    private User.Role role;
    
    /**
     * 账户是否启用
     */
    private Boolean enabled;

    /**
     * 从实体对象转换为DTO对象
     * 
     * @param user 用户实体对象
     * @return 用户DTO对象
     */
    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setEnabled(user.getEnabled());
        return dto;
    }
}

