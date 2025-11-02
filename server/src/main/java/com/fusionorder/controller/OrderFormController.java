package com.fusionorder.controller;

import com.fusionorder.dto.OrderFormDTO;
import com.fusionorder.entity.OrderForm;
import com.fusionorder.service.OrderFormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderFormController {
    private final OrderFormService orderFormService;

    @PostMapping
    public ResponseEntity<OrderFormDTO> createOrderForm(@Valid @RequestBody OrderForm orderForm) {
        return ResponseEntity.ok(orderFormService.createOrderForm(orderForm));
    }

    @GetMapping
    public ResponseEntity<List<OrderFormDTO>> getAllOrderForms(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) OrderForm.OrderStatus status) {
        if (productId != null) {
            return ResponseEntity.ok(orderFormService.getOrderFormsByProductId(productId));
        }
        if (status != null) {
            return ResponseEntity.ok(orderFormService.getOrderFormsByStatus(status));
        }
        return ResponseEntity.ok(orderFormService.getAllOrderForms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderFormDTO> getOrderFormById(@PathVariable Long id) {
        return ResponseEntity.ok(orderFormService.getOrderFormById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderFormDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderForm.OrderStatus status) {
        return ResponseEntity.ok(orderFormService.updateOrderFormStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderForm(@PathVariable Long id) {
        orderFormService.deleteOrderForm(id);
        return ResponseEntity.ok().build();
    }
}

