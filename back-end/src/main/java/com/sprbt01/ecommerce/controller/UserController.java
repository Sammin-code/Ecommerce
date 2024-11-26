package com.sprbt01.ecommerce.controller;

import com.sprbt01.ecommerce.dto.LoginResponse;
import com.sprbt01.ecommerce.model.User;
import com.sprbt01.ecommerce.dto.LoginRequest;
import com.sprbt01.ecommerce.dto.UserRequest;
import com.sprbt01.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Optional;
/**
 * Class Name: UserController
 * Package: com.sprbt01.ecommerce.controller
 * Description:該類為處理用戶相關操作的 REST 控制器，提供了註冊、登入及查詢用戶資訊等功能。
 * 使用 `UserService` 來進行用戶的註冊、驗證及查詢等邏輯操作。
 * author: Sammy
 * Create: 2024/8/26
 * Version: 1.0
 */

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService; // 自動注入 UserService，用於處理與用戶相關的邏輯

    @PostMapping("/register") // 處理用戶註冊的 POST 請求
    public ResponseEntity<User> registerUser(@RequestBody @Valid UserRequest userRequest) {
        // 使用 userService 註冊新用戶
        User newUser = userService.registerUser(userRequest);
        // 返回 201 Created 狀態及新註冊的用戶
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/login") // 處理用戶登入的 POST 請求
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        // 認證用戶，並獲取 JWT Token
        String token = userService.authenticateUser(loginRequest);
        // 根據用戶名查找用戶
        Optional<User> user = userService.findUserByUsername(loginRequest.getUsername());

        // 如果用戶存在，返回包含 Token 和用戶 ID 的回應
        if (user.isPresent()) {
            return ResponseEntity.ok(new LoginResponse(token, user.get().getId()));
        } else {
            // 如果用戶不存在，返回 401 Unauthorized 狀態
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{userId}") // 處理根據用戶 ID 查找用戶的 GET 請求
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        // 使用 userService 根據用戶 ID 獲取用戶
        User user = userService.getUserById(userId);
        // 如果用戶存在，返回 200 OK 狀態和用戶資料；若不存在，返回 404 Not Found 狀態
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        UserResponse response = new UserResponse(user.getId(), user.getRole().getName());
        return ResponseEntity.ok(response);
    }

    // 靜態內部類，用於響應用戶數據
    public static class UserResponse {
        private Long id;
        private String role;

        public UserResponse(Long id, String role) {
            this.id = id;
            this.role = role;
        }

        // Getter 和 Setter
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}





