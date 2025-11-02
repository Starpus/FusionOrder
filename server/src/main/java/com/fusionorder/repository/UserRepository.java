package com.fusionorder.repository;

import com.fusionorder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层
 * 提供用户数据的增删改查操作
 * 
 * @author FusionOrder Team
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 用户对象，如果不存在返回Optional.empty()
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据邮箱查找用户
     * 
     * @param email 邮箱
     * @return 用户对象，如果不存在返回Optional.empty()
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 如果存在返回true，否则返回false
     */
    Boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 如果存在返回true，否则返回false
     */
    Boolean existsByEmail(String email);
}

