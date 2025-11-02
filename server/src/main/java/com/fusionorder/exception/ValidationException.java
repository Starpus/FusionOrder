package com.fusionorder.exception;

/**
 * 验证异常
 * 当参数校验失败时抛出此异常
 */
public class ValidationException extends RuntimeException {
    
    /**
     * 构造函数
     * @param message 错误消息
     */
    public ValidationException(String message) {
        super(message);
    }
}

