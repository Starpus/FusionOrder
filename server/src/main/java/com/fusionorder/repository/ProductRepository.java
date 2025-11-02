package com.fusionorder.repository;

import com.fusionorder.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 产品数据访问层
 * 提供产品数据的增删改查操作，包括按分类、可用性、名称查询
 * 
 * @author FusionOrder Team
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * 根据分类查询产品列表
     * 
     * @param category 产品分类
     * @return 产品列表
     */
    List<Product> findByCategory(String category);
    
    /**
     * 查询所有可用产品
     * 
     * @return 可用产品列表
     */
    List<Product> findByAvailableTrue();
    
    /**
     * 根据产品名称模糊查询（不区分大小写）
     * 
     * @param name 产品名称关键词
     * @return 匹配的产品列表
     */
    List<Product> findByNameContainingIgnoreCase(String name);
}

