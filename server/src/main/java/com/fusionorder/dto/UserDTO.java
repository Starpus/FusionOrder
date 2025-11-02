package com.fusionorder.dto;

import com.fusionorder.entity.User;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private User.Role role;
    private Boolean enabled;

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

