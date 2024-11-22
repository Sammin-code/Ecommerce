package com.sprbt01.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/**
 * Class Name: CartItem
 * Package: com.sprbt01.ecommerce.cart
 * Description:表示購物車中的單個商品項目。每個 CartItem 與一個 Cart（購物車）和一個 Product（產品）相關聯，包含購買的數量。
 * author:
 * Create: 2024/9/5
 * Version: 1.0
 */
@Entity
public class CartItem {
    /**
     * CartItem 的唯一識別字，由系統自動產生。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 與 Cart（購物車）的多對一關係，用來表示該 CartItem 所屬的購物車。
     * 使用 @JsonBackReference 防止 JSON 反序列化時的循環引用。
     */

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "cart_id")
    private Cart cart;
    /**
     * 與 Product（產品）的多對一關係，多個用戶的購物車中都包含相同的商品。
     */

    @ManyToOne@JoinColumn(name = "product_id")
    private Product product;
    /**
     * 表示此商品在購物車中的數量，默認為 1。
     */
    private Integer quantity;

    public CartItem() {
        this.quantity = 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
