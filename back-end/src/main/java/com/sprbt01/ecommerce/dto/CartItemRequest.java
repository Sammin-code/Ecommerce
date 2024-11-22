package com.sprbt01.ecommerce.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Class Name: CartItemRequest
 * Package: com.sprbt01.ecommerce.dto
 * Description:類別用於封裝加入購物車的請求數據，包括產品 ID 和數量。它用於接收客戶端傳來的數據，並進行驗證。
 * author:
 * Create: 2024/9/9
 * Version: 1.0
 */
public class CartItemRequest {

    @NotNull(message = "Product ID is required") // 檢查 productId 不為空，若為空則顯示錯誤信息
    private Long productId; // 產品 ID，用於辨認加入購物車的產品

    @NotNull(message = "Quantity is required") // 檢查 quantity 不為空，若為空則顯示錯誤信息
    private Integer quantity; // 數量，用於指定加入購物車的產品數量

    // Getter 和 Setter 方法

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

