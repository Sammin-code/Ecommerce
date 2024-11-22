package com.sprbt01.ecommerce.dto;

/**
 * Class Name: LoginRequest
 * Package: com.sprbt01.ecommerce.dto
 * Description:該類別封裝了用戶登入後的回應數據，主要包括授權的 JWT token 和用戶 ID。用於向客戶端返回成功登入後的授權信息。
 * author:
 * Create: 2024/9/9
 * Version: 1.0
 */
public class LoginResponse {
    private String token;
    private Long userId;

    /**
     * 無參數建構子。
     */
    public LoginResponse() {
    }

    /**
     * 帶參數建構子，用於初始化 token 和 userId 的值。
     *
     * @param token 用於授權的 JWT token。
     * @param userId 登入使用者的 ID。
     */
    public LoginResponse(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    /**
     * 獲取 token 值。
     *
     * @return 返回 JWT token 字串。
     */
    public String getToken() {
        return token;
    }

    /**
     * 設置 token 值。
     *
     * @param token JWT token 字串。
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 獲取 userId 值。
     *
     * @return 返回使用者的 ID。
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 設置 userId 值。
     *
     * @param userId 使用者的 ID。
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
