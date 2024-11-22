package com.sprbt01.ecommerce.service;

import com.sprbt01.ecommerce.dto.ProductRequest;
import com.sprbt01.ecommerce.model.Product;
import com.sprbt01.ecommerce.exception.ResourceNotFoundException;
import com.sprbt01.ecommerce.repository.CartItemRepository;
import com.sprbt01.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class Name: ProductService
 * Package: com.sprbt01.ecommerce.service
 * Description:提供商品的 CRUD 操作，包括添加、更新、刪除和查詢商品等功能。
 * author: Sammy
 * Create: 2024/8/26
 * Version: 1.0
 */
@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;
    /**
     * 獲取所有商品
     * @return 商品列表
     */
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }
    /**
     * 根據商品ID查找商品
     * @param id 商品ID
     * @return 對應的商品，若不存在則拋出異常
     */
    public Product getProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }
    /**
     * 增加新商品
     * @param productRequest 新商品的請求數據
     * @return 已保存的商品
     */
    public Product addProduct(ProductRequest productRequest){
        Product product = new Product();

        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setQuantity(productRequest.getQuantity());

        return productRepository.save(product);
    }
    /**
     * 更新現有商品
     * @param productId 商品ID
     * @param productRequest 更新的商品數據
     * @return 更新後的商品
     */
    public Product updateProduct(Long productId,ProductRequest productRequest){

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        existingProduct.setName(productRequest.getName());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setQuantity(productRequest.getQuantity());

        return productRepository.save(existingProduct);
    }
    /**
     * 刪除商品
     * @param id 商品ID
     * @return 刪除成功返回 true
     */
    public Boolean deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }

        productRepository.deleteById(id);
        return true;
    }
    /**
     * 保存商品
     * @param product 要保存的商品
     */
    public void saveProduct(Product product) {
        if (product != null) {
            productRepository.save(product);
            System.out.println("產品已保存: " + product.getName());
        } else {
            throw new IllegalArgumentException("產品不能為空");
        }
    }
}
