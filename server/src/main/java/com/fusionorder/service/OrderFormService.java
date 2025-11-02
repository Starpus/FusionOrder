package com.fusionorder.service;

import com.fusionorder.dto.OrderFormDTO;
import com.fusionorder.entity.OrderForm;
import com.fusionorder.entity.Product;
import com.fusionorder.repository.OrderFormRepository;
import com.fusionorder.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderFormService {
    private final OrderFormRepository orderFormRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderFormDTO createOrderForm(OrderForm orderForm) {
        Product product = productRepository.findById(orderForm.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("产品不存在"));
        
        orderForm.setProduct(product);
        OrderForm savedOrder = orderFormRepository.save(orderForm);
        return OrderFormDTO.fromEntity(savedOrder);
    }

    public List<OrderFormDTO> getAllOrderForms() {
        return orderFormRepository.findAll().stream()
                .map(OrderFormDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<OrderFormDTO> getOrderFormsByProductId(Long productId) {
        return orderFormRepository.findByProductId(productId).stream()
                .map(OrderFormDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<OrderFormDTO> getOrderFormsByStatus(OrderForm.OrderStatus status) {
        return orderFormRepository.findByStatus(status).stream()
                .map(OrderFormDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public OrderFormDTO getOrderFormById(Long id) {
        OrderForm orderForm = orderFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        return OrderFormDTO.fromEntity(orderForm);
    }

    @Transactional
    public OrderFormDTO updateOrderFormStatus(Long id, OrderForm.OrderStatus status) {
        OrderForm orderForm = orderFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        orderForm.setStatus(status);
        OrderForm updatedOrder = orderFormRepository.save(orderForm);
        return OrderFormDTO.fromEntity(updatedOrder);
    }

    @Transactional
    public void deleteOrderForm(Long id) {
        if (!orderFormRepository.existsById(id)) {
            throw new RuntimeException("订单不存在");
        }
        orderFormRepository.deleteById(id);
    }
}

