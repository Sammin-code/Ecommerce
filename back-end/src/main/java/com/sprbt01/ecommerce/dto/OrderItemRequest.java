package com.sprbt01.ecommerce.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Class Name: OrderItemRequest
 * Package: com.sprbt01.ecommerce.dto
 * Description:此類別封裝了用於創建訂單的產品信息請求數據，包括產品 ID 和數量。主要用於接收客戶端提交的訂購項目數據。
 * author:
 * Create: 2024/9/9
 * Version: 1.0
 */
public class OrderItemRequest {

    /**
     * productId 表示產品的唯一識別字，為必填欄位。
     */
    @NotNull(message = "Product ID is required")
    private Long productId;

    /**
     * quantity 表示訂購產品的數量，為必填欄位。
     */
    @NotNull(message = "Quantity is required")
    private Integer quantity;

    // Getter 和 Setter 方法

    /**
     * 獲取 productId 值。
     *
     * @return 返回產品的唯一識別字。
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * 設置 productId 值。
     *
     * @param productId 訂單中產品的唯一識別字。
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * 獲取 quantity 值。
     *
     * @return 返回訂購產品的數量。
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 設置 quantity 值。
     *
     * @param quantity 訂購產品的數量。
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

