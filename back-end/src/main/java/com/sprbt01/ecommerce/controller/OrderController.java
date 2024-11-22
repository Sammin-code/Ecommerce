package com.sprbt01.ecommerce.controller;

import com.sprbt01.ecommerce.dto.OrderRequest;
import com.sprbt01.ecommerce.model.User;
import com.sprbt01.ecommerce.service.CartService;
import com.sprbt01.ecommerce.service.OrderService;
import com.sprbt01.ecommerce.model.Order;
import com.sprbt01.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.Principal;
import java.util.List;

/**
 * Class Name: OrderController
 * Package: com.sprbt01.ecommerce.controller
 * Description:該類為處理訂單相關操作的 REST 控制器，主要負責管理使用者的訂單。提供了根據使用者 ID 獲取所有訂單的功能，並提供確認訂單的端點。
 * 使用 `OrderService`、`UserService` 和 `CartService` 來執行訂單管理、使用者查詢和購物車操作等服務。
 * author:
 * Create: 2024/9/5
 * Version: 1.0
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    // 注入 OrderService 以便使用訂單相關的服務
    @Autowired
    private OrderService orderService;

    // 注入 UserService 以便利用使用者相關服務
    @Autowired
    private UserService userService;

    // 注入 CartService 以便使用購物車相關的服務
    @Autowired
    private CartService cartService;

    /**
     * 根據使用者 ID 獲取該使用者的所有訂單
     * @param userId 使用者 ID
     * @return 包含訂單列表的 200 回應
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    /**
     * 確認訂單，將指定的商品從購物車中移至訂單
     * @param userId 使用者 ID
     * @param productId 商品 ID
     * @return 如果訂單確認成功，返回 200回應和確認訊息；若商品不在購物車中，返回 400 回應和錯誤訊息；若出現例外情況，返回 500 回應和錯誤訊息
     */
    @PostMapping("/confirm/{userId}/{productId}")
    public ResponseEntity<?> confirmOrder(@PathVariable Long userId, @PathVariable Long productId) {
        try {
            // 檢查商品是否存在於指定使用者的購物車中
            boolean itemExists = cartService.isProductInCart(userId, productId);
            if (!itemExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item not found in cart.");
            }

            // 創建訂單
            orderService.createOrder(userId, productId);
            return ResponseEntity.ok("Order confirmed ");
        } catch (Exception e) {
            // 若確認訂單失敗，記錄錯誤並返回 500 回應
            System.out.println("Order confirmation failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error confirming order: " + e.getMessage());
        }
    }
}

