package com.sprbt01.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * Class Name: Role
 * Package: com.sprbt01.ecommerce.model
 * Description:表示用戶角色的類別，用於定義不同類型用戶的權限。例如：ADMIN、USER等。每個角色可以關聯多個用戶。
 * author: Sammy
 * Create: 2024/8/26
 * Version: 1.0
 */
@Entity
public class Role {
    /**
     * Role 的唯一識別字，由系統自動產生。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // 角色名稱
    private String name;

    // 與 User 的一對多關係，映射到 User 中的 "role" 屬性，並設置級聯操作
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<User> users = new ArrayList<>();

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    // Getters and setters
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
