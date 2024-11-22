package com.sprbt01.ecommerce.config;

import com.sprbt01.ecommerce.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Class Name: JwtTokenProvider
 * Package: com.sprbt01.ecommerce.config
 * Description:JwtTokenProvider 類別負責生成、解析和驗證 JSON Web Token (JWT)。
 *  *       它使用 HS256 加密算法來簽名 JWT，並提供方法來創建、驗證及提取 token 中的用戶信息。
 *  *       此類別主要用於處理與 JWT 相關的操作，保證用戶的身份驗證過程安全且有效。
 * author:
 * Create: 2024/10/13
 * Version: 1.0
 */
@Component
public class JwtTokenProvider {

    // 使用 HS256 演算法生成的 SecretKey，用於簽署和驗證 JWT
    SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Token 的有效期間（以毫秒為單位），此處設定為 1 天
    private final long EXPIRATION_TIME = 86400000;

    /**
     * 根據用戶名和角色創建 JWT。
     *
     * @param username 用戶名
     * @param role 用戶的角色
     * @return 生成的 JWT 字符串
     */
    public String createToken(String username, Role role) {
        // 設置 JWT 中的自訂內容 (claims)，包括用戶角色
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.getName());

        // 使用用戶名和角色生成 Token，設定簽發時間和過期時間
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username) // 設定主體（用戶名）
                .setIssuedAt(new Date(System.currentTimeMillis())) // 設定簽發時間
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 設定過期時間
                .signWith(SignatureAlgorithm.HS256, secretKey) // 使用 SecretKey 簽名
                .compact();
        System.out.println("Generated Token: " + token); // 輸出產生的 Token 以供檢查
        return token;
    }

    /**
     * 從給定的 JWT 中提取用戶名。
     *
     * @param token JWT 字符串
     * @return 提取出的用戶名
     */
    public String getUsernameFromToken(String token) {
        // 使用 SecretKey 驗證並解析 JWT，然後提取主體 (用戶名)
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * 驗證 JWT 是否有效。
     *
     * @param token JWT 字符串
     * @return 若 Token 有效且未過期則返回 true，否則返回 false
     */
    public boolean validateToken(String token) {
        try {
            // 檢查 Token 是否過期
            return !isTokenExpired(token);
        } catch (SignatureException e) {
            // 如果簽名不符合，則 Token 無效
            System.out.println("Invalid JWT signature: " + e.getMessage());
            return false;
        } catch (Exception e) {
            // catch其他可能的驗證錯誤
            System.out.println("JWT validation failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * 檢查 JWT 是否已過期。
     *
     * @param token JWT 字符串
     * @return 若 Token 已過期則返回 true，否則返回 false
     */
    private boolean isTokenExpired(String token) {
        // 從 JWT 中提取過期時間，並與當前時間進行比較
        Date expiration = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date()); // 若過期時間早於當前時間，則視為過期
    }
}