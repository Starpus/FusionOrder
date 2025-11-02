package com.fusionorder.controller;

import com.fusionorder.dto.ProductDTO;
import com.fusionorder.entity.Product;
import com.fusionorder.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(@RequestParam(required = false) Boolean available,
                                                           @RequestParam(required = false) String category,
                                                           @RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return ResponseEntity.ok(productService.searchProducts(keyword));
        }
        if (category != null && !category.isEmpty()) {
            return ResponseEntity.ok(productService.getProductsByCategory(category));
        }
        if (Boolean.TRUE.equals(available)) {
            return ResponseEntity.ok(productService.getAvailableProducts());
        }
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}

