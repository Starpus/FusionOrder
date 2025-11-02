package com.fusionorder.exception;

/**
 * 业务异常
 * 当业务逻辑处理过程中出现错误时抛出此异常
 */
public class BusinessException extends RuntimeException {
    
    /**
     * 构造函数
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
    }
}

