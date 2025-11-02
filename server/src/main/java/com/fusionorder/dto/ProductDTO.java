package com.fusionorder.dto;

import com.fusionorder.entity.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private Boolean available;
    private LocalDateTime createdAt;

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

