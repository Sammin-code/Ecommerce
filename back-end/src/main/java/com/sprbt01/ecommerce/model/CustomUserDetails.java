package com.sprbt01.ecommerce.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Class Name: CustomUserDetails
 * Package: com.sprbt01.ecommerce.model
 * Description:此類別將自訂的 User 封裝為符合 Spring Security 需求的 UserDetails，並提供必要的安全性資料，如用戶名和密碼。
 * author:
 * Create: 2024/10/21
 * Version: 1.0
 */
public class CustomUserDetails implements UserDetails {
    // 內部持有一個 User 實例，該實例包含了本系統使用者的相關資料
    private final com.sprbt01.ecommerce.model.User user;
    /**
     * 建構子，接收一個 User 物件並初始化
     * @param user 系統中的使用者物件
     */
    public CustomUserDetails(com.sprbt01.ecommerce.model.User user) {
        this.user = user;
    }
    /**
     * 獲取用戶的授權集合（權限）。
     * @return 此使用者的權限集合。此處為空列表，表示目前無授權設定。
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return java.util.List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

}
