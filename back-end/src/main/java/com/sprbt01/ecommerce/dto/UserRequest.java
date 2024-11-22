package com.sprbt01.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Class Name: UserRequest
 * Package: com.sprbt01.ecommerce.dto
 * Description:此類別封裝了用於創建或註冊新用戶的請求數據，包括用戶名、密碼和電子郵件地址。主要用於接收客戶端提交的用戶信息，並對其進行基礎驗證。
 * author:
 * Create: 2024/9/9
 * Version: 1.0
 */
public class UserRequest {
    /**
     * username 表示用戶名，為必填欄位。
     */
    @NotBlank(message = "Username is required")
    private String username;

    /**
     * password 表示用戶密碼，為必填欄位，並且必須至少包含 6 個字符。
     */
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    /**
     * email 表示用戶的電子郵件地址，為必填欄位。
     */
    @NotBlank(message = "email is required")
    private  String email;



    // Getter and Setter methods

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

    public  String getEmail() {
        return email;
    }

    public void setEmail( String email) {
        this.email = email;
    }
}
