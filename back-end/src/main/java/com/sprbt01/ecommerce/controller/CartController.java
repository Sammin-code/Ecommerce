package com.sprbt01.ecommerce.controller;

import com.sprbt01.ecommerce.model.Cart;
import com.sprbt01.ecommerce.dto.CartItemRequest;
import com.sprbt01.ecommerce.model.CartItem;
import com.sprbt01.ecommerce.model.User;
import com.sprbt01.ecommerce.repository.CartRepository;
import com.sprbt01.ecommerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

/**
 * Class Name: CartController
 * Package: com.sprbt01.ecommerce.controller
 * Description:該類是處理使用者購物車相關操作的 REST 控制器。包括獲取使用者購物車、向購物車添加商品、從購物車移除商品和清空購物車等功能。
 *  * 每個方法都通過 HTTP 請求與客戶端交互，執行對應的購物車管理操作，並返回回應。
 * author:
 * Create: 2024/9/5
 * Version: 1.0
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    // 注入 CartService 以便使用購物車相關的服務
    @Autowired
    private CartService cartService;

    /**
     * 根據使用者 ID 獲取該使用者的購物車
     * @param userId 使用者 ID
     * @return 如果找到使用者的購物車，返回 200 回應和購物車物件；如果未找到，返回 404 回應
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCartByUser(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return cart != null ? ResponseEntity.ok(cart) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * 向指定使用者的購物車中添加商品
     * @param userId 使用者 ID
     * @param cartItemRequest 包含商品 ID 和數量的Request Body
     * @return 返回更新後的購物車物件
     */
    @PostMapping("add/{userId}")
    public ResponseEntity<Cart> addItemToCart(@PathVariable Long userId, @RequestBody @Valid CartItemRequest cartItemRequest) {
        // 如果未指定數量，預設設為 1
        cartItemRequest.setQuantity(Optional.ofNullable(cartItemRequest.getQuantity()).orElse(1));
        Cart cart = cartService.addItemToCart(userId, cartItemRequest);
        return ResponseEntity.ok(cart);
    }

    /**
     * 從購物車中移除指定商品
     * @param productId 商品 ID
     * @param principal 包含當前使用者資訊的 Principal 物件
     * @return 如果成功移除商品，返回 200 回應和提示資訊；否則返回 404 回應和錯誤資訊
     */
    @DeleteMapping("remove/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long productId, Principal principal) {
        boolean removed = cartService.removeFromCart(productId, principal.getName());
        if (removed) {
            return ResponseEntity.ok("Item removed from cart");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found in cart");
        }
    }

    /**
     * 清空當前使用者的購物車
     * @param principal 包含當前使用者資訊的 Principal 物件
     * @return 如果成功清空購物車，返回 200 回應和提示資訊；否則返回 404 回應和錯誤資訊
     */
    @DeleteMapping("/removeAll")
    public ResponseEntity<?> removeAllFromCart(Principal principal) {
        boolean removed = cartService.removeAllFromCart(principal.getName());
        if (removed) {
            return ResponseEntity.ok("All items removed from cart");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No items found in cart");
        }
    }
}

