package com.sprbt01.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sprbt01.ecommerce.exception.ResourceNotFoundException;
import com.sprbt01.ecommerce.repository.UserRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Name: Cart
 * Package: com.sprbt01.ecommerce
 * Description:購物車類別，包含用戶與購物車項目的關聯。每個購物車屬於一個用戶，並可包含多個購物車項目。
 * author:
 * Create: 2024/9/5
 * Version: 1.0
 */
@Entity
public class Cart {
    /**
     * 購物車的唯一識別字，自動產生。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 購物車所屬的用戶，與 User 類別建立多對一關聯。
     */
    @ManyToOne
    private User user;
    /**
     * 購物車中包含的商品項目列表，與 CartItem 類別建立一對多的關聯。
     * 這些商品項目會在購物車清空時被一起刪除 (cascade = CascadeType.ALL)，並且在 CartItem 被移除時會被自動清理 (orphanRemoval = true)。
     */
    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CartItem> cartItems = new ArrayList<>();
    /**
     * 將商品項目（CartItem）加入到購物車（Cart）中，並且會自動設置這個 CartItem 所屬的購物車為當前的 Cart 實例，商品項目和購物車互相建立關聯。
     *
     * @param item 要添加的購物車項目
     */
    public void addCartItem(CartItem item) {
        cartItems.add(item);
        item.setCart(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

}

