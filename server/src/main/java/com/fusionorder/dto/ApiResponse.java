package com.fusionorder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 统一API响应格式
 * 所有接口返回统一使用此格式，便于前端统一处理
 * 
 * @param <T> 响应数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    /**
     * 响应状态码
     * 200: 成功
     * 400: 客户端错误
     * 401: 未授权
     * 403: 禁止访问
     * 404: 资源不存在
     * 500: 服务器错误
     */
    private Integer code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 响应时间戳
     */
    private LocalDateTime timestamp;
    
    /**
     * 成功响应（无数据）
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "success", null, LocalDateTime.now());
    }
    
    /**
     * 成功响应（带数据）
     * @param data 响应数据
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data, LocalDateTime.now());
    }
    
    /**
     * 成功响应（自定义消息）
     * @param message 响应消息
     * @param data 响应数据
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data, LocalDateTime.now());
    }
    
    /**
     * 错误响应
     * @param code 错误码
     * @param message 错误消息
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message, null, LocalDateTime.now());
    }
    
    /**
     * 错误响应（400）
     * @param message 错误消息
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(400, message, null, LocalDateTime.now());
    }
    
    /**
     * 错误响应（401）
     * @param message 错误消息
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<>(401, message, null, LocalDateTime.now());
    }
    
    /**
     * 错误响应（403）
     * @param message 错误消息
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(403, message, null, LocalDateTime.now());
    }
    
    /**
     * 错误响应（404）
     * @param message 错误消息
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(404, message, null, LocalDateTime.now());
    }
    
    /**
     * 错误响应（500）
     * @param message 错误消息
     * @return ApiResponse对象
     */
    public static <T> ApiResponse<T> internalError(String message) {
        return new ApiResponse<>(500, message, null, LocalDateTime.now());
    }
}

