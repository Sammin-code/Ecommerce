package com.sprbt01.ecommerce.repository;

import com.sprbt01.ecommerce.model.User;
import com.sprbt01.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Class Name: CartRepository
 * Package: com.sprbt01.ecommerce.repository
 * Description:提供對 Cart 實體的資料庫操作，包括查找特定用戶的購物車。
 * author:
 * Create: 2024/9/5
 * Version: 1.0
 */
public interface CartRepository extends JpaRepository<Cart, Long> {
    /**
     * 根據用戶查找購物車。
     * @param user 用戶實體
     * @return 返回指定用戶的 Cart
     */
    Cart findByUser(User user);
    /**
     * 根據用戶 ID 查找購物車。
     * @param userId 用戶 ID
     * @return 返回包含指定用戶的 Cart 的 Optional
     */
    Optional<Cart> findByUserId(Long userId);


}
