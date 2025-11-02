package com.fusionorder.dto;

import com.fusionorder.entity.OrderForm;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderFormDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private String requirements;
    private OrderForm.OrderStatus status;
    private LocalDateTime createdAt;

    public static OrderFormDTO fromEntity(OrderForm orderForm) {
        OrderFormDTO dto = new OrderFormDTO();
        dto.setId(orderForm.getId());
        dto.setProductId(orderForm.getProduct().getId());
        dto.setProductName(orderForm.getProduct().getName());
        dto.setQuantity(orderForm.getQuantity());
        dto.setContactName(orderForm.getContactName());
        dto.setContactPhone(orderForm.getContactPhone());
        dto.setContactEmail(orderForm.getContactEmail());
        dto.setRequirements(orderForm.getRequirements());
        dto.setStatus(orderForm.getStatus());
        dto.setCreatedAt(orderForm.getCreatedAt());
        return dto;
    }
}

