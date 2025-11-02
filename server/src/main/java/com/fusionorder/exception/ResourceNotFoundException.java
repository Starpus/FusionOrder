package com.fusionorder.exception;

/**
 * 资源不存在异常
 * 当查询的资源不存在时抛出此异常
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * 构造函数
     * @param message 错误消息
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * 构造函数
     * @param resourceName 资源名称
     * @param id 资源ID
     */
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s不存在，ID: %d", resourceName, id));
    }
}

