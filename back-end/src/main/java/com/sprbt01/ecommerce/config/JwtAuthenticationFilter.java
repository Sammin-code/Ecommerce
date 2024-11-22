package com.sprbt01.ecommerce.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Class Name: JwtAuthorizationFilter
 * Package: com.sprbt01.ecommerce.config
 * Description:JwtAuthenticationFilter 是一個過濾器，用於攔截每個 HTTP 請求，
 *  *       從中檢查是否包含有效的 JWT（JSON Web Token）以進行用戶身份驗證。
 *  *       若存在有效的 JWT，則會將用戶的身份驗證信息設定到安全上下文中。
 * author:
 * Create: 2024/10/13
 * Version: 1.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // 用於產生和驗證 JWT 的工具類

    @Autowired
    private UserDetailsService userDetailsService; // 根據用戶名載入用戶詳情的服務

    /**
     * 每次請求都會執行此過濾器，用來檢查請求中是否存在有效的 JWT。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 從請求中取得 JWT
        String token = getTokenFromRequest(request);

        // 驗證 token 是否有效
        if (token != null && jwtTokenProvider.validateToken(token)) {

            // 從 token 中取得用戶名
            String username = jwtTokenProvider.getUsernameFromToken(token);

            // 載入用戶詳情
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 創建身份驗證對象，設定當前用戶的身份驗證信息
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 將身份驗證信息設定到安全上下文中
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 繼續執行下一個過濾器
        filterChain.doFilter(request, response);
    }

    /**
     * 從請求頭中提取 JWT。如果存在且符合 "Bearer " 開頭格式，則返回 token。
     *
     * @param request 客戶端請求
     * @return 提取的 JWT，如果不存在或格式不正確，則返回 null
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // 移除 "Bearer " 前綴並返回 token
        }
        return null; // 返回 null 表示請求標頭中沒有有效的 JWT
    }
}
