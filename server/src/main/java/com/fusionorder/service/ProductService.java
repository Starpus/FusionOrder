package com.fusionorder.service;

import com.fusionorder.dto.ProductDTO;
import com.fusionorder.entity.Product;
import com.fusionorder.exception.ResourceNotFoundException;
import com.fusionorder.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品服务类
 * 负责产品相关的业务逻辑处理，包括产品增删改查、分类查询、搜索等功能
 * 
 * @author FusionOrder Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    
    /**
     * 产品数据访问层
     */
    private final ProductRepository productRepository;

    /**
     * 创建产品
     * 
     * @param product 产品实体对象
     * @return 产品DTO对象
     */
    @Transactional
    public ProductDTO createProduct(Product product) {
        log.info("开始创建产品, name: {}, category: {}", product.getName(), product.getCategory());
        Product savedProduct = productRepository.save(product);
        log.info("创建产品成功, productId: {}, name: {}", savedProduct.getId(), savedProduct.getName());
        return ProductDTO.fromEntity(savedProduct);
    }

    /**
     * 获取所有产品列表
     * 
     * @return 产品DTO列表
     */
    public List<ProductDTO> getAllProducts() {
        log.info("查询所有产品");
        List<ProductDTO> products = productRepository.findAll().stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
        log.info("查询到 {} 个产品", products.size());
        return products;
    }

    /**
     * 获取所有可用产品列表
     * 
     * @return 可用产品DTO列表
     */
    public List<ProductDTO> getAvailableProducts() {
        log.info("查询所有可用产品");
        List<ProductDTO> products = productRepository.findByAvailableTrue().stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
        log.info("查询到 {} 个可用产品", products.size());
        return products;
    }

    /**
     * 根据分类获取产品列表
     * 
     * @param category 产品分类
     * @return 产品DTO列表
     */
    public List<ProductDTO> getProductsByCategory(String category) {
        log.info("按分类查询产品, category: {}", category);
        List<ProductDTO> products = productRepository.findByCategory(category).stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
        log.info("查询到 {} 个产品", products.size());
        return products;
    }

    /**
     * 根据关键词搜索产品
     * 在产品名称中模糊匹配关键词（不区分大小写）
     * 
     * @param keyword 搜索关键词
     * @return 产品DTO列表
     */
    public List<ProductDTO> searchProducts(String keyword) {
        log.info("搜索产品, keyword: {}", keyword);
        List<ProductDTO> products = productRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
        log.info("搜索到 {} 个产品", products.size());
        return products;
    }

    /**
     * 根据ID获取产品信息
     * 
     * @param id 产品ID
     * @return 产品DTO对象
     * @throws ResourceNotFoundException 产品不存在时抛出
     */
    public ProductDTO getProductById(Long id) {
        log.info("查询产品, productId: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("产品不存在, productId: {}", id);
                    return new ResourceNotFoundException("产品", id);
                });
        log.info("查询产品成功, productId: {}, name: {}", id, product.getName());
        return ProductDTO.fromEntity(product);
    }

    /**
     * 更新产品信息
     * 支持部分更新，只更新提供的字段
     * 
     * @param id 产品ID
     * @param productDetails 要更新的产品信息
     * @return 更新后的产品DTO对象
     * @throws ResourceNotFoundException 产品不存在时抛出
     */
    @Transactional
    public ProductDTO updateProduct(Long id, Product productDetails) {
        log.info("开始更新产品, productId: {}", id);
        
        // 查找产品
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("更新失败：产品不存在, productId: {}", id);
                    return new ResourceNotFoundException("产品", id);
                });
        
        // 更新产品名称
        if (productDetails.getName() != null) {
            product.setName(productDetails.getName());
            log.debug("更新产品名称, newName: {}", productDetails.getName());
        }
        
        // 更新产品分类
        if (productDetails.getCategory() != null) {
            product.setCategory(productDetails.getCategory());
            log.debug("更新产品分类, newCategory: {}", productDetails.getCategory());
        }
        
        // 更新产品价格
        if (productDetails.getPrice() != null) {
            product.setPrice(productDetails.getPrice());
            log.debug("更新产品价格, newPrice: {}", productDetails.getPrice());
        }
        
        // 更新产品描述
        if (productDetails.getDescription() != null) {
            product.setDescription(productDetails.getDescription());
        }
        
        // 更新产品图片URL
        if (productDetails.getImageUrl() != null) {
            product.setImageUrl(productDetails.getImageUrl());
        }
        
        // 更新产品可用性
        if (productDetails.getAvailable() != null) {
            product.setAvailable(productDetails.getAvailable());
            log.debug("更新产品可用性, available: {}", productDetails.getAvailable());
        }
        
        // 保存更新后的产品
        Product updatedProduct = productRepository.save(product);
        log.info("更新产品成功, productId: {}, name: {}", id, updatedProduct.getName());
        
        return ProductDTO.fromEntity(updatedProduct);
    }

    /**
     * 删除产品
     * 
     * @param id 产品ID
     * @throws ResourceNotFoundException 产品不存在时抛出
     */
    @Transactional
    public void deleteProduct(Long id) {
        log.info("开始删除产品, productId: {}", id);
        
        if (!productRepository.existsById(id)) {
            log.warn("删除失败：产品不存在, productId: {}", id);
            throw new ResourceNotFoundException("产品", id);
        }
        
        productRepository.deleteById(id);
        log.info("删除产品成功, productId: {}", id);
    }
}

