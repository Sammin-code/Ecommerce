package com.sprbt01.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sprbt01.ecommerce.service.UserService;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Name: Order
 * Package: com.sprbt01.ecommerce.model
 * Description:用於表示訂單的實體類，包含訂單的詳細資訊，如訂單 ID、訂單日期、總金額、用戶和訂單項目等。
 * 訂單與用戶之間存在多對一關係，與訂單項目之間存在一對多的關係。
 * author:
 * Create: 2024/9/4
 * Version: 1.0
 */


@Entity
@Table(name = "orders")
public class Order {
    /**
     * Order 的唯一識別字，由系統自動產生。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 訂單日期和時間，以 "yyyy-MM-dd'T'HH:mm:ss" 格式儲存
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime orderDate;
    // 訂單總金額
    private BigDecimal totalAmount;
    // 與 User 的多對一關係，表示下訂單的用戶
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    // 與 OrderItem 的一對多關係，表示訂單中的各個項目
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {}

    // Getters 和 Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void setUserId(Long userId, UserService userService) {
        User foundUser = userService.getUserById(userId);
        if (foundUser != null) {
            this.user = foundUser;
        } else {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
    }
}
