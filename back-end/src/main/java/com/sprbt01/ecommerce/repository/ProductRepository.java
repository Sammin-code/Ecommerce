package com.sprbt01.ecommerce.repository;

import com.sprbt01.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Class Name: ProductRepository
 * Package: com.sprbt01.ecommerce.repository
 * Description:提供對 Product 實體的數據訪問，包括根據產品名稱查找產品。
 * author: Sammy
 * Create: 2024/8/26 上午 10:55
 * Version: 1.0
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * 根據產品名稱查找產品。
     * @param name 產品名稱
     * @return 返回包含指定名稱的 Product 的 Optional
     */
    Optional<Product> findByName(String name);
}
