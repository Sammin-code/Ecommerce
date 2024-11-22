package com.sprbt01.ecommerce.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Class Name: OrderRequest
 * Package: com.sprbt01.ecommerce.dto
 * Description:此類別封裝了用於創建新訂單的請求數據，包括用戶 ID 和訂單項目列表。主要用於接收客戶端提交的訂單信息。
 * author:
 * Create: 2024/9/9
 * Version: 1.0
 */
public class OrderRequest {
    /**
     * userId 表示下訂單的使用者 ID，為必填欄位。
     */
    @NotNull(message = "User ID is required")
    private Long userId;
    /**
     * items 表示訂單中包含的產品項目列表，必須至少包含一個訂單項目。
     */
    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemRequest> items;

    // Getter and Setter methods

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}

