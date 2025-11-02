package com.fusionorder.service;

import com.fusionorder.dto.ProductDTO;
import com.fusionorder.entity.Product;
import com.fusionorder.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public ProductDTO createProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return ProductDTO.fromEntity(savedProduct);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getAvailableProducts() {
        return productRepository.findByAvailableTrue().stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("产品不存在"));
        return ProductDTO.fromEntity(product);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("产品不存在"));
        
        if (productDetails.getName() != null) {
            product.setName(productDetails.getName());
        }
        
        if (productDetails.getCategory() != null) {
            product.setCategory(productDetails.getCategory());
        }
        
        if (productDetails.getPrice() != null) {
            product.setPrice(productDetails.getPrice());
        }
        
        if (productDetails.getDescription() != null) {
            product.setDescription(productDetails.getDescription());
        }
        
        if (productDetails.getImageUrl() != null) {
            product.setImageUrl(productDetails.getImageUrl());
        }
        
        if (productDetails.getAvailable() != null) {
            product.setAvailable(productDetails.getAvailable());
        }
        
        Product updatedProduct = productRepository.save(product);
        return ProductDTO.fromEntity(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("产品不存在");
        }
        productRepository.deleteById(id);
    }
}

