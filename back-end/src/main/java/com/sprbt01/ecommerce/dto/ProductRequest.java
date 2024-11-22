package com.sprbt01.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Class Name: ProductRequest
 * Package: com.sprbt01.ecommerce.dto
 * Description:此類別封裝了創建和更新產品時所需的請求數據，包括產品名稱、價格、描述和數量。主要用於接收客戶端提交的產品信息，並對數據進行基礎驗證。
 * author:
 * Create: 2024/9/9
 * Version: 1.0
 */
public class ProductRequest {
    /**
     * name 表示產品名稱，為必填欄位。
     */
    @NotBlank(message = "Product name is required")
    private String name;
    /**
     * price 表示產品價格，為必填欄位。
     */
    @NotNull(message = "Price is required")
    private Double price;

    private String description;
    private Integer quantity;

    // Getter and Setter methods

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
