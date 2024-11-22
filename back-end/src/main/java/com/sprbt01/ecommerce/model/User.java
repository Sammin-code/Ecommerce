package com.sprbt01.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class Name: User
 * Package: com.sprbt01.ecommerce.User
 * Description:示系統中的用戶，包含用戶的基本資訊、角色以及訂單等屬性。該類別可以用於處理認證、授權等功能。
 * author: Sammy
 * Create: 2024/9/1 下午 12:12
 * Version: 1.0
 */
@Entity
public class User {
    /**
     * User 的唯一識別字，由系統自動產生。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // 用戶名，通常用於登入
    private String username;

    // 密碼
    private String password;

    // 用戶的電子郵件地址
    private String email;

    // 與 Order 的一對多關係，用於記錄用戶的訂單
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    // 與 Role 的多對一關係，表示此用戶的角色
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    @JsonBackReference
    private Role role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User() {
    }

    public User(Long id, String username, String password, String email, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
    public void addRole(Role role) {
        if (this.role == null) {
            this.role = role;
            System.out.println("Role assigned: " + role.getName());
        } else {
            System.out.println("User already has a role: " + this.role.getName());
        }
    }


}

