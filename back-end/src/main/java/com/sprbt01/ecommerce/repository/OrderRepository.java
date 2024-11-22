package com.sprbt01.ecommerce.repository;

import com.sprbt01.ecommerce.model.User;
import com.sprbt01.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Class Name: OrderRepository
 * Package: com.sprbt01.ecommerce.repository
 * Description:提供對 Order 實體的資料庫操作，包括查找特定用戶的訂單。
 * author:
 * Create: 2024/9/4
 * Version: 1.0
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * 根據用戶查找訂單列表。
     * @param user 用戶實體
     * @return 返回指定用戶的所有 Order 列表
     */
    List<Order> findByUser(User user);
}
