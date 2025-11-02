package com.fusionorder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 订单表单实体类
 * 存储客户提交的订货信息，包括产品、数量、联系方式、需求等
 * 
 * @author FusionOrder Team
 */
@Entity
@Table(name = "order_forms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderForm {
    
    /**
     * 订单ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 关联的产品，多对一关系
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "产品不能为空")
    private Product product;
    
    /**
     * 订货数量，不能为空，最小值1
     */
    @Column(nullable = false)
    @NotNull(message = "订货数量不能为空")
    @Min(value = 1, message = "订货数量必须大于0")
    private Integer quantity;
    
    /**
     * 联系人姓名，不能为空，最大长度50
     */
    @Column(name = "contact_name", nullable = false, length = 50)
    @NotBlank(message = "联系人姓名不能为空")
    @Size(max = 50, message = "联系人姓名长度不能超过50")
    private String contactName;
    
    /**
     * 联系人电话，不能为空，最大长度20
     */
    @Column(name = "contact_phone", nullable = false, length = 20)
    @NotBlank(message = "联系人电话不能为空")
    @Size(max = 20, message = "联系人电话长度不能超过20")
    private String contactPhone;
    
    /**
     * 联系人邮箱，可为空，最大长度100
     */
    @Column(name = "contact_email", length = 100)
    @Email(message = "邮箱格式不正确")
    private String contactEmail;
    
    /**
     * 客户需求，文本类型
     */
    @Column(columnDefinition = "TEXT")
    private String requirements;
    
    /**
     * 订单状态，枚举类型，默认PENDING
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING;
    
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
     * 订单状态枚举
     */
    public enum OrderStatus {
        /**
         * 待处理
         */
        PENDING,
        
        /**
         * 已确认
         */
        CONFIRMED,
        
        /**
         * 处理中
         */
        PROCESSING,
        
        /**
         * 已完成
         */
        COMPLETED,
        
        /**
         * 已取消
         */
        CANCELLED
    }
}

