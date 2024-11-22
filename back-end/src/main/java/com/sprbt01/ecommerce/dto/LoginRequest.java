package com.sprbt01.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Class Name: LoginRequest
 * Package: com.sprbt01.ecommerce.dto
 * Description:該類別封裝了用戶登入請求的數據，包括用戶名和密碼。主要用於接收客戶端發送的登入請求數據，並對其進行基礎驗證。
 * author:
 * Create: 2024/9/9
 * Version: 1.0
 */
public class LoginRequest {

    @NotBlank(message = "Username is required") // 檢查 username 不為空或空白，若為空白則顯示錯誤信息
    private String username; // 用戶名，供用戶註冊或登入使用

    @NotBlank(message = "Password is required") // 檢查 password 不為空或空白，若為空白則顯示錯誤信息
    private String password; // 密碼，用於用戶身份驗證

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
}