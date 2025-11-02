package com.fusionorder.repository;

import com.fusionorder.entity.OrderForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderFormRepository extends JpaRepository<OrderForm, Long> {
    List<OrderForm> findByProductId(Long productId);
    List<OrderForm> findByStatus(OrderForm.OrderStatus status);
}

