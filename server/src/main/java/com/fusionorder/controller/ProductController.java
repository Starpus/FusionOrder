package com.fusionorder.controller;

import com.fusionorder.dto.ApiResponse;
import com.fusionorder.dto.ProductDTO;
import com.fusionorder.entity.Product;
import com.fusionorder.service.ProductService;
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
 * 产品管理控制器
 * 提供产品相关的增删改查接口
 * 
 * @author FusionOrder Team
 */
@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "产品管理", description = "产品相关的增删改查接口，支持搜索、分类筛选等功能")
public class ProductController {
    
    /**
     * 产品服务
     */
    private final ProductService productService;

    /**
     * 获取产品列表
     * 支持按关键词搜索、分类筛选、可用性筛选
     * 所有用户可访问
     * 
     * @param available 是否可用（true: 仅可用产品，false/null: 所有产品）
     * @param category 产品分类
     * @param keyword 搜索关键词（产品名称）
     * @return 产品列表
     */
    @GetMapping
    @Operation(summary = "获取产品列表", description = "支持按分类、关键词搜索、可用性筛选产品，所有用户可访问")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查询成功", 
                content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "参数错误")
    })
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts(
            @Parameter(description = "是否可用（true: 仅可用产品，false/null: 所有产品）") 
            @RequestParam(required = false) Boolean available,
            @Parameter(description = "产品分类") 
            @RequestParam(required = false) String category,
            @Parameter(description = "搜索关键词（产品名称）") 
            @RequestParam(required = false) String keyword) {
        
        List<ProductDTO> products;
        
        if (keyword != null && !keyword.isEmpty()) {
            log.info("搜索产品, keyword: {}", keyword);
            products = productService.searchProducts(keyword);
            log.info("搜索到 {} 个产品", products.size());
        } else if (category != null && !category.isEmpty()) {
            log.info("按分类查询产品, category: {}", category);
            products = productService.getProductsByCategory(category);
            log.info("查询到 {} 个产品", products.size());
        } else if (Boolean.TRUE.equals(available)) {
            log.info("查询可用产品");
            products = productService.getAvailableProducts();
            log.info("查询到 {} 个可用产品", products.size());
        } else {
            log.info("查询所有产品");
            products = productService.getAllProducts();
            log.info("查询到 {} 个产品", products.size());
        }
        
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * 根据ID获取产品详情
     * 所有用户可访问
     * 
     * @param id 产品ID
     * @return 产品信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取产品详情", description = "根据产品ID获取详细信息，所有用户可访问")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查询成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "产品不存在")
    })
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(
            @Parameter(description = "产品ID") @PathVariable Long id) {
        log.info("查询产品详情, productId: {}", id);
        ProductDTO product = productService.getProductById(id);
        log.info("查询产品成功, productId: {}, name: {}", id, product.getName());
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    /**
     * 创建产品
     * 需要管理员或产品管理员权限
     * 
     * @param product 产品信息
     * @return 创建的产品信息
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "创建产品", description = "创建新产品，需要管理员或产品管理员权限")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "创建成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "参数验证失败"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "权限不足")
    })
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
            @Parameter(description = "产品信息") @Valid @RequestBody Product product) {
        log.info("创建产品, name: {}, category: {}", product.getName(), product.getCategory());
        ProductDTO createdProduct = productService.createProduct(product);
        log.info("创建产品成功, productId: {}, name: {}", createdProduct.getId(), createdProduct.getName());
        return ResponseEntity.ok(ApiResponse.success("产品创建成功", createdProduct));
    }

    /**
     * 更新产品信息
     * 需要管理员或产品管理员权限
     * 
     * @param id 产品ID
     * @param product 产品信息
     * @return 更新后的产品信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "更新产品", description = "更新产品信息，需要管理员或产品管理员权限")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "更新成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "产品不存在"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "权限不足")
    })
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @Parameter(description = "产品ID") @PathVariable Long id,
            @Parameter(description = "产品信息") @Valid @RequestBody Product product) {
        log.info("更新产品, productId: {}", id);
        ProductDTO updatedProduct = productService.updateProduct(id, product);
        log.info("更新产品成功, productId: {}, name: {}", id, updatedProduct.getName());
        return ResponseEntity.ok(ApiResponse.success("产品更新成功", updatedProduct));
    }

    /**
     * 删除产品
     * 需要管理员或产品管理员权限
     * 
     * @param id 产品ID
     * @return 成功响应
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "删除产品", description = "删除产品，需要管理员或产品管理员权限")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "删除成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "产品不存在"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "权限不足")
    })
    public ResponseEntity<ApiResponse<Object>> deleteProduct(
            @Parameter(description = "产品ID") @PathVariable Long id) {
        log.info("删除产品, productId: {}", id);
        productService.deleteProduct(id);
        log.info("删除产品成功, productId: {}", id);
        return ResponseEntity.ok(ApiResponse.success("产品删除成功"));
    }
}

