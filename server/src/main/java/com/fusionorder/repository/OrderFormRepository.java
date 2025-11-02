package com.fusionorder.repository;

import com.fusionorder.entity.OrderForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单表单数据访问层
 * 提供订单表单数据的增删改查操作，包括按产品ID、状态查询
 * 
 * @author FusionOrder Team
 */
@Repository
public interface OrderFormRepository extends JpaRepository<OrderForm, Long> {
    
    /**
     * 根据产品ID查询订单列表
     * 
     * @param productId 产品ID
     * @return 订单列表
     */
    List<OrderForm> findByProductId(Long productId);
    
    /**
     * 根据订单状态查询订单列表
     * 
     * @param status 订单状态
     * @return 订单列表
     */
    List<OrderForm> findByStatus(OrderForm.OrderStatus status);
}

