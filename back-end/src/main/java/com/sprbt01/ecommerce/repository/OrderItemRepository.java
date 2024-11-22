package com.sprbt01.ecommerce.repository;

import com.sprbt01.ecommerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Class Name: OrderItemRepository
 * Package: com.sprbt01.ecommerce.repository
 * Description:提供對 OrderItem 實體的基本資料庫操作
 * author:
 * Create: 2024/10/31
 * Version: 1.0
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
