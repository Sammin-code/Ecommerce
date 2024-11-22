package com.sprbt01.ecommerce.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * Class Name: Product
 * Package: com.sprbt01.ecommerce.model
 * Description:用於表示電子商務應用中的產品，包括產品名稱、描述、價格和庫存量等屬性。
 * 此別類與 `CartItem` 有一對多關係，以便在購物車中存儲與此產品相關的多個購物車。
 * author: Sammy
 * Create: 2024/8/26 上午 09:59
 * Version: 1.0
 */
@Entity
public class Product {
    /**
     * Product 的唯一識別字，由系統自動產生。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 產品名稱
    private String name;

    // 產品描述
    private String description;

    // 產品價格
    private double price;

    // 產品庫存數量
    private int quantity;

    // 與 CartItem 的一對多關係，在產品刪除時將相關聯的 CartItem 也一併刪除
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    public Product() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product(Long id, String name, String description, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}




