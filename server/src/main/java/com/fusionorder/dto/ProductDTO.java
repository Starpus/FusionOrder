package com.fusionorder.dto;

import com.fusionorder.entity.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品数据传输对象
 * 用于在前端和后端之间传输产品信息
 * 
 * @author FusionOrder Team
 */
@Data
public class ProductDTO {
    
    /**
     * 产品ID
     */
    private Long id;
    
    /**
     * 产品名称
     */
    private String name;
    
    /**
     * 产品分类
     */
    private String category;
    
    /**
     * 产品价格
     */
    private BigDecimal price;
    
    /**
     * 产品描述
     */
    private String description;
    
    /**
     * 产品图片URL
     */
    private String imageUrl;
    
    /**
     * 产品是否可用
     */
    private Boolean available;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 从实体对象转换为DTO对象
     * 
     * @param product 产品实体对象
     * @return 产品DTO对象
     */
    public static ProductDTO fromEntity(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setImageUrl(product.getImageUrl());
        dto.setAvailable(product.getAvailable());
        dto.setCreatedAt(product.getCreatedAt());
        return dto;
    }
}

