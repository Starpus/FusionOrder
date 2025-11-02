package com.fusionorder.controller;

import com.fusionorder.dto.ApiResponse;
import com.fusionorder.dto.OrderFormDTO;
import com.fusionorder.entity.OrderForm;
import com.fusionorder.service.OrderFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单表单控制器
 * 提供订单表单的增删改查功能
 * 用户可以提交订单，管理员可以查看和管理订单
 * 
 * @author FusionOrder Team
 */
@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "订单管理", description = "订单表单相关的增删改查接口，支持订单提交、状态更新等功能")
public class OrderFormController {
    
    /**
     * 订单表单服务
     */
    private final OrderFormService orderFormService;

    /**
     * 创建订单表单
     * 所有用户可访问，用于提交订货信息
     * 
     * @param orderForm 订单表单信息
     * @return 创建的订单表单信息
     */
    @PostMapping
    @Operation(summary = "创建订单表单", description = "提交订货信息，所有用户可访问")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "订单提交成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "参数验证失败或产品不存在")
    })
    public ResponseEntity<ApiResponse<OrderFormDTO>> createOrderForm(
            @Parameter(description = "订单表单信息，包含产品ID、数量、联系方式等") 
            @Valid @RequestBody OrderForm orderForm) {
        log.info("创建订单表单, productId: {}, quantity: {}, contactName: {}", 
                orderForm.getProduct().getId(), orderForm.getQuantity(), orderForm.getContactName());
        OrderFormDTO orderFormDTO = orderFormService.createOrderForm(orderForm);
        log.info("创建订单表单成功, orderFormId: {}", orderFormDTO.getId());
        return ResponseEntity.ok(ApiResponse.success("订单提交成功", orderFormDTO));
    }

    /**
     * 获取订单表单列表
     * 支持按产品ID、订单状态筛选
     * 所有用户可访问，但仅管理员可查看所有订单
     * 
     * @param productId 产品ID（可选）
     * @param status 订单状态（可选）
     * @return 订单表单列表
     */
    @GetMapping
    @Operation(summary = "获取订单列表", description = "支持按产品ID、订单状态筛选，所有用户可访问")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查询成功")
    })
    public ResponseEntity<ApiResponse<List<OrderFormDTO>>> getAllOrderForms(
            @Parameter(description = "产品ID（可选）") @RequestParam(required = false) Long productId,
            @Parameter(description = "订单状态（可选）") @RequestParam(required = false) OrderForm.OrderStatus status) {
        
        List<OrderFormDTO> orderForms;
        
        if (productId != null) {
            log.info("按产品ID查询订单, productId: {}", productId);
            orderForms = orderFormService.getOrderFormsByProductId(productId);
            log.info("查询到 {} 个订单", orderForms.size());
        } else if (status != null) {
            log.info("按状态查询订单, status: {}", status);
            orderForms = orderFormService.getOrderFormsByStatus(status);
            log.info("查询到 {} 个订单", orderForms.size());
        } else {
            log.info("查询所有订单");
            orderForms = orderFormService.getAllOrderForms();
            log.info("查询到 {} 个订单", orderForms.size());
        }
        
        return ResponseEntity.ok(ApiResponse.success(orderForms));
    }

    /**
     * 根据ID获取订单表单详情
     * 所有用户可访问
     * 
     * @param id 订单表单ID
     * @return 订单表单信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取订单详情", description = "根据订单ID获取详细信息，所有用户可访问")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查询成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "订单不存在")
    })
    public ResponseEntity<ApiResponse<OrderFormDTO>> getOrderFormById(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        log.info("查询订单详情, orderFormId: {}", id);
        OrderFormDTO orderForm = orderFormService.getOrderFormById(id);
        log.info("查询订单成功, orderFormId: {}, status: {}", id, orderForm.getStatus());
        return ResponseEntity.ok(ApiResponse.success(orderForm));
    }

    /**
     * 更新订单状态
     * 需要管理员权限
     * 
     * @param id 订单表单ID
     * @param status 新状态
     * @return 更新后的订单表单信息
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新订单状态", description = "更新订单状态，仅管理员可访问")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "更新成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "订单不存在"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "权限不足")
    })
    public ResponseEntity<ApiResponse<OrderFormDTO>> updateOrderStatus(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "新状态") @RequestParam OrderForm.OrderStatus status) {
        log.info("更新订单状态, orderFormId: {}, newStatus: {}", id, status);
        OrderFormDTO updatedOrder = orderFormService.updateOrderFormStatus(id, status);
        log.info("更新订单状态成功, orderFormId: {}, status: {}", id, updatedOrder.getStatus());
        return ResponseEntity.ok(ApiResponse.success("订单状态更新成功", updatedOrder));
    }

    /**
     * 删除订单表单
     * 需要管理员权限
     * 
     * @param id 订单表单ID
     * @return 成功响应
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除订单", description = "删除订单，仅管理员可访问")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "删除成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "订单不存在"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "权限不足")
    })
    public ResponseEntity<ApiResponse<Object>> deleteOrderForm(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        log.info("删除订单, orderFormId: {}", id);
        orderFormService.deleteOrderForm(id);
        log.info("删除订单成功, orderFormId: {}", id);
        return ResponseEntity.ok(ApiResponse.success("订单删除成功"));
    }
}

