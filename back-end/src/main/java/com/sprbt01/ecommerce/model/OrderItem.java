package com.sprbt01.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Class Name: OrderItem
 * Package: order
 * Description:用於表示訂單中的單個項目，包含產品、數量和單價等資訊。
 * 與訂單和產品實體存在多對一關係，代表每個訂單項目都屬於一個訂單，並且對應到一個具體的產品。
 * author:
 * Create: 2024/9/4
 * Version: 1.0
 */
@Entity
public class OrderItem {
    /**
     * OrderItem 的唯一識別字，由系統自動產生。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 與 Product 的多對一關係，表示此訂單項目對應的產品
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_product_order_item", foreignKeyDefinition = "ON UPDATE CASCADE"))
    private Product product;
    // 與 Order 的多對一關係，表示此訂單項目隸屬於哪一個訂單
    @ManyToOne
    @JsonBackReference
    private Order order;

    // 訂單項目的數量
    private Integer quantity;
    // 訂單項目的單價
    private BigDecimal price;

    // getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OrderItem() {
    }

    public OrderItem(Long id, Product product, Order order, Integer quantity, BigDecimal price) {
        this.id = id;
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.price = price;
    }

    public void add(OrderItem orderItem) {

    }
}
