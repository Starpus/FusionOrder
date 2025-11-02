package com.fusionorder.dto;

import com.fusionorder.entity.OrderForm;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单表单数据传输对象
 * 用于在前端和后端之间传输订单信息，包含产品基本信息
 * 
 * @author FusionOrder Team
 */
@Data
public class OrderFormDTO {
    
    /**
     * 订单ID
     */
    private Long id;
    
    /**
     * 产品ID
     */
    private Long productId;
    
    /**
     * 产品名称
     */
    private String productName;
    
    /**
     * 订货数量
     */
    private Integer quantity;
    
    /**
     * 联系人姓名
     */
    private String contactName;
    
    /**
     * 联系人电话
     */
    private String contactPhone;
    
    /**
     * 联系人邮箱
     */
    private String contactEmail;
    
    /**
     * 客户需求
     */
    private String requirements;
    
    /**
     * 订单状态
     */
    private OrderForm.OrderStatus status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 从实体对象转换为DTO对象
     * 
     * @param orderForm 订单表单实体对象
     * @return 订单表单DTO对象
     */
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

