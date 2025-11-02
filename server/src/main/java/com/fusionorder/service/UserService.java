package com.fusionorder.service;

import com.fusionorder.dto.UserDTO;
import com.fusionorder.entity.User;
import com.fusionorder.exception.BusinessException;
import com.fusionorder.exception.ResourceNotFoundException;
import com.fusionorder.exception.ValidationException;
import com.fusionorder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务类
 * 负责用户相关的业务逻辑处理，包括注册、查询、更新、删除等功能
 * 
 * @author FusionOrder Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    
    /**
     * 用户数据访问层
     */
    private final UserRepository userRepository;
    
    /**
     * 密码编码器，用于加密密码
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户注册
     * 验证用户名和邮箱的唯一性，加密密码后保存用户信息
     * 
     * @param user 用户实体对象
     * @return 用户DTO对象
     * @throws BusinessException 用户名或邮箱已存在时抛出
     */
    @Transactional
    public UserDTO register(User user) {
        log.info("开始注册用户, username: {}", user.getUsername());
        
        // 验证用户名是否已存在
        if (userRepository.existsByUsername(user.getUsername())) {
            log.warn("注册失败：用户名已存在, username: {}", user.getUsername());
            throw new BusinessException("用户名已存在");
        }
        
        // 验证邮箱是否已存在
        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            log.warn("注册失败：邮箱已存在, email: {}", user.getEmail());
            throw new BusinessException("邮箱已存在");
        }
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 设置默认角色为USER
        if (user.getRole() == null) {
            user.setRole(User.Role.USER);
        }
        
        // 保存用户
        User savedUser = userRepository.save(user);
        log.info("用户注册成功, userId: {}, username: {}", savedUser.getId(), savedUser.getUsername());
        
        return UserDTO.fromEntity(savedUser);
    }

    /**
     * 获取所有用户列表
     * 
     * @return 用户DTO列表
     */
    public List<UserDTO> getAllUsers() {
        log.info("查询所有用户");
        List<UserDTO> users = userRepository.findAll().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
        log.info("查询到 {} 个用户", users.size());
        return users;
    }

    /**
     * 根据ID获取用户信息
     * 
     * @param id 用户ID
     * @return 用户DTO对象
     * @throws ResourceNotFoundException 用户不存在时抛出
     */
    public UserDTO getUserById(Long id) {
        log.info("查询用户, userId: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("用户不存在, userId: {}", id);
                    return new ResourceNotFoundException("用户", id);
                });
        log.info("查询用户成功, userId: {}, username: {}", id, user.getUsername());
        return UserDTO.fromEntity(user);
    }

    /**
     * 更新用户信息
     * 支持部分更新，只更新提供的字段
     * 
     * @param id 用户ID
     * @param userDetails 要更新的用户信息
     * @return 更新后的用户DTO对象
     * @throws ResourceNotFoundException 用户不存在时抛出
     * @throws BusinessException 用户名或邮箱已存在时抛出
     */
    @Transactional
    public UserDTO updateUser(Long id, User userDetails) {
        log.info("开始更新用户, userId: {}", id);
        
        // 查找用户
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("更新失败：用户不存在, userId: {}", id);
                    return new ResourceNotFoundException("用户", id);
                });
        
        // 更新用户名（如果提供且不同）
        if (userDetails.getUsername() != null && !userDetails.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(userDetails.getUsername())) {
                log.warn("更新失败：用户名已存在, username: {}", userDetails.getUsername());
                throw new BusinessException("用户名已存在");
            }
            user.setUsername(userDetails.getUsername());
            log.debug("更新用户名, newUsername: {}", userDetails.getUsername());
        }
        
        // 更新邮箱（如果提供且不同）
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                log.warn("更新失败：邮箱已存在, email: {}", userDetails.getEmail());
                throw new BusinessException("邮箱已存在");
            }
            user.setEmail(userDetails.getEmail());
            log.debug("更新邮箱, newEmail: {}", userDetails.getEmail());
        }
        
        // 更新电话号码
        if (userDetails.getPhone() != null) {
            user.setPhone(userDetails.getPhone());
        }
        
        // 更新角色
        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
            log.debug("更新角色, newRole: {}", userDetails.getRole());
        }
        
        // 更新启用状态
        if (userDetails.getEnabled() != null) {
            user.setEnabled(userDetails.getEnabled());
            log.debug("更新启用状态, enabled: {}", userDetails.getEnabled());
        }
        
        // 更新密码（如果提供）
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            log.debug("更新密码");
        }
        
        // 保存更新后的用户
        User updatedUser = userRepository.save(user);
        log.info("更新用户成功, userId: {}, username: {}", id, updatedUser.getUsername());
        
        return UserDTO.fromEntity(updatedUser);
    }

    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @throws ResourceNotFoundException 用户不存在时抛出
     */
    @Transactional
    public void deleteUser(Long id) {
        log.info("开始删除用户, userId: {}", id);
        
        if (!userRepository.existsById(id)) {
            log.warn("删除失败：用户不存在, userId: {}", id);
            throw new ResourceNotFoundException("用户", id);
        }
        
        userRepository.deleteById(id);
        log.info("删除用户成功, userId: {}", id);
    }
}

