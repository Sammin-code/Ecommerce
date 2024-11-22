package com.sprbt01.ecommerce.service;

import com.sprbt01.ecommerce.model.User;
import com.sprbt01.ecommerce.model.Cart;
import com.sprbt01.ecommerce.model.CartItem;
import com.sprbt01.ecommerce.dto.CartItemRequest;
import com.sprbt01.ecommerce.model.Product;
import com.sprbt01.ecommerce.exception.ResourceNotFoundException;
import com.sprbt01.ecommerce.repository.CartItemRepository;
import com.sprbt01.ecommerce.repository.CartRepository;
import com.sprbt01.ecommerce.repository.ProductRepository;
import com.sprbt01.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Class Name: CartService
 * Package: com.sprbt01.ecommerce.service
 * Description:提供購物車相關的服務，包括添加商品、移除商品、清空購物車以及檢查商品是否存在於購物車中。
 *  * author: 您的名稱
 * author:
 * Create: 2024/9/5
 * Version: 1.0
 */
@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    /**
     * 根據用戶 ID 獲取購物車。
     * @param userId 用戶 ID
     * @return 返回指定用戶的 Cart
     */
    public Cart getCartByUserId(Long userId) {
        User user = new User();
        user.setId(userId);
        return cartRepository.findByUser(user);
    }
    /**
     * 添加商品到指定用戶的購物車。
     * @param userId 用戶 ID
     * @param cartItemRequest 包含商品 ID 和數量的請求
     * @return 返回更新後的購物車 Cart
     */
    public Cart addItemToCart(Long userId, CartItemRequest cartItemRequest) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + cartItemRequest.getProductId()));

        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + cartItemRequest.getQuantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setProduct(product);
            cart.addCartItem(cartItem);
        }
        return cartRepository.save(cart);
    }
    /**
     * 從用戶的購物車中移除商品。
     * @param productId 商品 ID
     * @param username 用戶名
     * @return 成功移除返回 true，否則返回 false
     */
    public boolean removeFromCart(Long productId, String username) {
        User user = userService.findByUsername(username);
        Optional<CartItem> cartItemOptional = cartItemRepository.findByProduct_IdAndCart_User(productId, user);
        if (cartItemOptional.isPresent()) {
            cartItemRepository.delete(cartItemOptional.get());
            return true;
        }
        return false;
    }
    /**
     * 清空用戶的購物車。
     * @param username 用戶名
     * @return 成功清空返回 true，否則返回 false
     */
    public boolean removeAllFromCart(String username) {
        User user = userService.findByUsername(username);
        List<CartItem> cartItems = cartItemRepository.findByCart_User(user);

        if (!cartItems.isEmpty()) {
            cartItemRepository.deleteAll(cartItems);
            return true;
        }
        return false;
    }
    /**
     * 從購物車中移除指定商品，並刷新購物車。
     * @param userId 用戶 ID
     * @param productId 商品 ID
     */
    @Transactional
    public void removeProductFromCart(Long userId, Long productId) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        if (!optionalCart.isPresent()) {
            throw new RuntimeException("The cart isn't found");
        }

        Cart cart = optionalCart.get();
        System.out.println("Current cart items before removal: " + cart.getCartItems());

        Optional<CartItem> itemToRemove = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (itemToRemove.isPresent()) {
            cart.getCartItems().remove(itemToRemove.get());
            cartRepository.save(cart);
            cartRepository.flush();
            System.out.println("Product removed from cart.");
            System.out.println("Current cart items after removal: " + cart.getCartItems());
        } else {
            throw new RuntimeException("Item not found in the cart.");
        }
    }
    /**
     * 檢查指定用戶的購物車中是否存在指定商品。
     * @param userId 用戶 ID
     * @param productId 商品 ID
     * @return 如果商品存在於購物車中返回 true，否則返回 false
     */
    public boolean isProductInCart(Long userId, Long productId) {
        return cartItemRepository.existsByCartUserIdAndProductId(userId, productId);
    }
}
