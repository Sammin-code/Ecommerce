package com.sprbt01.ecommerce.config;

import com.sprbt01.ecommerce.model.User;
import com.sprbt01.ecommerce.service.CustomUserDetailsService;
import com.sprbt01.ecommerce.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Class Name: WebSecurityConfig
 * Package: com.sprbt01.ecommerce.config
 * Description:
 * author: Sammy
 * Create: 2024/8/27 下午 02:37
 * Version: 1.0
 */
@Configuration
@EnableWebMvc
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @Lazy
    private UserService userService; // 延遲注入 UserService，避免循環依賴

    @Autowired
    private CustomUserDetailsService customUserDetailsService; // 自定義的 UserDetailsService，用於載入用戶詳細信息

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // JWT 生成和驗證的提供者

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter; // 自定義的 JWT 認證過濾器

    /**
     * 配置 Spring Security 的主要安全規則。
     * @param http HttpSecurity 配置對象
     * @return SecurityFilterChain 安全過濾器鏈
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 設置授權規則
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // 公共路徑，所有人可以訪問
                                .requestMatchers("/", "/api/products", "/api/users/login", "/error", "/api/users/register").permitAll()
                                // 僅 ADMIN 角色可訪問的路徑
                                .requestMatchers("/admin/**", "/api/orders/all").hasRole("ADMIN")
                                // 其他路徑需要身份驗證
                                .anyRequest().authenticated()
                )
                // 配置表單登錄
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // 自定義登錄頁面
                                .defaultSuccessUrl("/", true) // 登錄成功後跳轉到首頁
                                .permitAll()
                )
                // 配置登出功能
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/") // 登出成功後跳轉到首頁
                                .permitAll()
                )
                // 禁用 CSRF 防護（適用於 JWT 無狀態的 API 認證）
                .csrf(csrfConfigurer -> csrfConfigurer.disable())
                // 配置異常處理
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint((request, response, authException) -> {
                                    // 當身份驗證失敗時，返回 JSON 格式的 401 錯誤信息
                                    response.setContentType("application/json");
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.getWriter().write("{\"error\":\"Unauthorized\"}");
                                })
                )
                .cors(withDefaults()) // 啟用默認的 CORS 配置
                // 使用無狀態的會話管理，適用於 JWT
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 在 UsernamePasswordAuthenticationFilter 之前添加自定義 JWT 認證過濾器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 配置 AuthenticationManager，用於身份驗證。
     * @param authenticationConfiguration 提供身份驗證配置
     * @return AuthenticationManager 身份驗證管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 配置 PasswordEncoder，使用 BCrypt 進行密碼編碼。
     * @return PasswordEncoder 密碼編碼器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置 CORS 規則。
     * @return WebMvcConfigurer 配置 CORS 規則的配置器
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                System.out.println("CORS configuration applied.");
                registry.addMapping("/api/**") // 配置 CORS 規則僅應用於 /api/** 路徑
                        .allowedOrigins("http://localhost:8080") // 設定允許的來源
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 設定允許的 HTTP 方法
                        .allowedHeaders("*") // 設定允許的標頭
                        .allowCredentials(true); // 允許憑證
            }
        };
    }
}


