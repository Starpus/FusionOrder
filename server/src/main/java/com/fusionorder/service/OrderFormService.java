package com.fusionorder.service;

import com.fusionorder.dto.OrderFormDTO;
import com.fusionorder.entity.OrderForm;
import com.fusionorder.entity.Product;
import com.fusionorder.exception.ResourceNotFoundException;
import com.fusionorder.repository.OrderFormRepository;
import com.fusionorder.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单表单服务类
 * 负责订单表单相关的业务逻辑处理，包括订单创建、查询、状态更新等功能
 * 
 * @author FusionOrder Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderFormService {
    
    /**
     * 订单表单数据访问层
     */
    private final OrderFormRepository orderFormRepository;
    
    /**
     * 产品数据访问层
     */
    private final ProductRepository productRepository;

    /**
     * 创建订单表单
     * 验证产品是否存在，然后创建订单
     * 
     * @param orderForm 订单表单实体对象
     * @return 订单表单DTO对象
     * @throws ResourceNotFoundException 产品不存在时抛出
     */
    @Transactional
    public OrderFormDTO createOrderForm(OrderForm orderForm) {
        log.info("开始创建订单表单, productId: {}, quantity: {}", 
                orderForm.getProduct().getId(), orderForm.getQuantity());
        
        // 验证产品是否存在
        Product product = productRepository.findById(orderForm.getProduct().getId())
                .orElseThrow(() -> {
                    log.warn("创建订单失败：产品不存在, productId: {}", orderForm.getProduct().getId());
                    return new ResourceNotFoundException("产品", orderForm.getProduct().getId());
                });
        
        // 设置产品信息
        orderForm.setProduct(product);
        
        // 保存订单
        OrderForm savedOrder = orderFormRepository.save(orderForm);
        log.info("创建订单表单成功, orderFormId: {}, productId: {}, quantity: {}", 
                savedOrder.getId(), savedOrder.getProduct().getId(), savedOrder.getQuantity());
        
        return OrderFormDTO.fromEntity(savedOrder);
    }

    /**
     * 获取所有订单表单列表
     * 
     * @return 订单表单DTO列表
     */
    public List<OrderFormDTO> getAllOrderForms() {
        log.info("查询所有订单表单");
        List<OrderFormDTO> orderForms = orderFormRepository.findAll().stream()
                .map(OrderFormDTO::fromEntity)
                .collect(Collectors.toList());
        log.info("查询到 {} 个订单", orderForms.size());
        return orderForms;
    }

    /**
     * 根据产品ID获取订单表单列表
     * 
     * @param productId 产品ID
     * @return 订单表单DTO列表
     */
    public List<OrderFormDTO> getOrderFormsByProductId(Long productId) {
        log.info("按产品ID查询订单, productId: {}", productId);
        List<OrderFormDTO> orderForms = orderFormRepository.findByProductId(productId).stream()
                .map(OrderFormDTO::fromEntity)
                .collect(Collectors.toList());
        log.info("查询到 {} 个订单", orderForms.size());
        return orderForms;
    }

    /**
     * 根据状态获取订单表单列表
     * 
     * @param status 订单状态
     * @return 订单表单DTO列表
     */
    public List<OrderFormDTO> getOrderFormsByStatus(OrderForm.OrderStatus status) {
        log.info("按状态查询订单, status: {}", status);
        List<OrderFormDTO> orderForms = orderFormRepository.findByStatus(status).stream()
                .map(OrderFormDTO::fromEntity)
                .collect(Collectors.toList());
        log.info("查询到 {} 个订单", orderForms.size());
        return orderForms;
    }

    /**
     * 根据ID获取订单表单信息
     * 
     * @param id 订单表单ID
     * @return 订单表单DTO对象
     * @throws ResourceNotFoundException 订单不存在时抛出
     */
    public OrderFormDTO getOrderFormById(Long id) {
        log.info("查询订单表单, orderFormId: {}", id);
        OrderForm orderForm = orderFormRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("订单不存在, orderFormId: {}", id);
                    return new ResourceNotFoundException("订单", id);
                });
        log.info("查询订单表单成功, orderFormId: {}, status: {}", id, orderForm.getStatus());
        return OrderFormDTO.fromEntity(orderForm);
    }

    /**
     * 更新订单状态
     * 
     * @param id 订单表单ID
     * @param status 新状态
     * @return 更新后的订单表单DTO对象
     * @throws ResourceNotFoundException 订单不存在时抛出
     */
    @Transactional
    public OrderFormDTO updateOrderFormStatus(Long id, OrderForm.OrderStatus status) {
        log.info("开始更新订单状态, orderFormId: {}, newStatus: {}", id, status);
        
        // 查找订单
        OrderForm orderForm = orderFormRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("更新订单状态失败：订单不存在, orderFormId: {}", id);
                    return new ResourceNotFoundException("订单", id);
                });
        
        // 更新状态
        OrderForm.OrderStatus oldStatus = orderForm.getStatus();
        orderForm.setStatus(status);
        OrderForm updatedOrder = orderFormRepository.save(orderForm);
        
        log.info("更新订单状态成功, orderFormId: {}, oldStatus: {}, newStatus: {}", 
                id, oldStatus, status);
        
        return OrderFormDTO.fromEntity(updatedOrder);
    }

    /**
     * 删除订单表单
     * 
     * @param id 订单表单ID
     * @throws ResourceNotFoundException 订单不存在时抛出
     */
    @Transactional
    public void deleteOrderForm(Long id) {
        log.info("开始删除订单表单, orderFormId: {}", id);
        
        if (!orderFormRepository.existsById(id)) {
            log.warn("删除订单失败：订单不存在, orderFormId: {}", id);
            throw new ResourceNotFoundException("订单", id);
        }
        
        orderFormRepository.deleteById(id);
        log.info("删除订单表单成功, orderFormId: {}", id);
    }
}

