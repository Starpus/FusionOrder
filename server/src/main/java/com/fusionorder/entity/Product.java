package com.fusionorder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品实体类
 * 存储产品的详细信息，包括名称、分类、价格、描述、图片等
 * 
 * @author FusionOrder Team
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    /**
     * 产品ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 产品名称，不能为空，最大长度100
     */
    @Column(nullable = false, length = 100)
    @NotBlank(message = "产品名称不能为空")
    @Size(max = 100, message = "产品名称长度不能超过100")
    private String name;
    
    /**
     * 产品分类，不能为空，最大长度50
     */
    @Column(nullable = false, length = 50)
    @NotBlank(message = "产品分类不能为空")
    @Size(max = 50, message = "产品分类长度不能超过50")
    private String category;
    
    /**
     * 产品价格，不能为空，精度10位，小数点后2位，最小值0.01
     */
    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "产品价格不能为空")
    @DecimalMin(value = "0.01", message = "产品价格必须大于0")
    private BigDecimal price;
    
    /**
     * 产品描述，文本类型
     */
    @Column(columnDefinition = "TEXT")
    private String description;
    
    /**
     * 产品图片URL，最大长度255
     */
    @Column(name = "image_url", length = 255)
    private String imageUrl;
    
    /**
     * 产品是否可用，不能为空，默认true
     */
    @Column(nullable = false)
    private Boolean available = true;
    
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
}

