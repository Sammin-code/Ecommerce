package com.sprbt01.ecommerce.controller;

import com.sprbt01.ecommerce.dto.ProductRequest;
import com.sprbt01.ecommerce.model.Product;
import com.sprbt01.ecommerce.model.User;
import com.sprbt01.ecommerce.repository.ProductRepository;
import com.sprbt01.ecommerce.repository.UserRepository;
import com.sprbt01.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class Name: ProductController
 * Package: com.sprbt01.ecommerce.controller
 * Description:該類為處理產品相關操作的 REST 控制器，提供了查詢所有產品、查詢當前使用者資訊、檢查產品名稱、添加產品、以及刪除產品等功能。
 * 使用 `ProductService`、`UserRepository` 和 `ProductRepository` 來執行產品管理和使用者數據的操作。
 * author: Sammy
 * Create: 2024/8/26 上午 11:19
 * Version: 1.0
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService; // 自動注入 ProductService，用於處理產品的服務

    @Autowired
    private UserRepository userRepository; // 自動注入 UserRepository，用於存取和操作用戶數據

    @Autowired
    private ProductRepository productRepository; // 自動注入 ProductRepository，用於存取和操作產品數據

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        // 調用 productService 獲取所有產品的清單
        List<Product> products = productService.getAllProducts();
        // 回傳包含產品列表的回應
        return ResponseEntity.ok(products);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        // 如果未驗證，返回未授權的回應
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The visit isn't authorized");
        }

        // 獲取當前用戶的詳細信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // 根據用戶名查找用戶
        Optional<User> optionalUser = userRepository.findByUsername(username);

        // 如果找不到用戶，返回 404 錯誤
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();
        String role = user.getRole().getName();

        // 構建包含用戶名和角色的回應
        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("roles", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkName")
    @PreAuthorize("hasRole('ADMIN')") // 限制此方法只有 ADMIN 角色可以訪問
    public ResponseEntity<?> checkProductName(@RequestParam("name") String name) {
        // 檢查產品名稱是否已存在
        Optional<Product> existingProduct = productRepository.findByName(name);
        if (existingProduct.isPresent()) {
            // 如果名稱存在，返回 409 衝突狀態
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Product name already exists.");
        }
        // 如果名稱可用，返回 OK 狀態
        return ResponseEntity.ok("Product name is available");
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')") // 限制此方法只有 ADMIN 角色可以訪問
    public ResponseEntity<Product> addProduct(@RequestBody @Valid ProductRequest productRequest) {
        // 根據請求創建新的產品
        Product newProduct = new Product();
        newProduct.setName(productRequest.getName());
        newProduct.setPrice(productRequest.getPrice());
        newProduct.setDescription(productRequest.getDescription());
        newProduct.setQuantity(productRequest.getQuantity());

        // 保存新產品並返回
        Product savedProduct = productRepository.save(newProduct);
        return ResponseEntity.ok(savedProduct);
    }

    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasRole('ADMIN')") // 限制此方法只有 ADMIN 角色可以訪問
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        // 調用 productService 刪除產品
        boolean isDeleted = productService.deleteProduct(productId);
        // 如果成功刪除，返回 204 無內容狀態，否則返回 404 狀態
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
