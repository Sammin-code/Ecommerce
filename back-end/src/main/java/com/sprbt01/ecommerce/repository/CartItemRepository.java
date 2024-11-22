package com.sprbt01.ecommerce.repository;

import com.sprbt01.ecommerce.model.Cart;
import com.sprbt01.ecommerce.model.CartItem;
import com.sprbt01.ecommerce.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Class Name: CartItemRepository
 * Package: com.sprbt01.ecommerce.repository
 * Description:提供對 CartItem 實體的資料庫操作，包含查詢指定用戶的購物車商品和檢查商品是否已存在於購物車中。
 * author:
 * Create: 2024/9/10
 * Version: 1.0
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    /**
     * 根據商品 ID 和用戶查找特定用戶的購物車項目。
     * @param productId 商品 ID
     * @param user 用戶實體
     * @return 返回包含符合條件的 CartItem 的 Optional
     */
    Optional<CartItem> findByProduct_IdAndCart_User(Long productId, User user);
    /**
     * 查找特定用戶的所有購物車項目。
     * @param user 用戶實體
     * @return 返回用戶的所有 CartItem 列表
     */
    List<CartItem> findByCart_User(User user);
    /**
     * 檢查特定用戶的購物車中是否已包含指定商品。
     * @param userId 用戶 ID
     * @param productId 商品 ID
     * @return 如果存在該商品，則返回 true，否則返回 false
     */
    boolean existsByCartUserIdAndProductId(Long userId, Long productId);

}
