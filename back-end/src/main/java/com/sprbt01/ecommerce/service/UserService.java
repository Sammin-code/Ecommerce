package com.sprbt01.ecommerce.service;

import com.sprbt01.ecommerce.config.JwtTokenProvider;
import com.sprbt01.ecommerce.model.Role;
import com.sprbt01.ecommerce.model.User;
import com.sprbt01.ecommerce.dto.LoginRequest;
import com.sprbt01.ecommerce.dto.UserRequest;
import com.sprbt01.ecommerce.repository.RoleRepository;
import com.sprbt01.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Class Name: UserService
 * Package: com.sprbt01.ecommerce.service
 * Description:提供用戶相關服務，包括用戶註冊、身份驗證、角色設定等功能
 * author: Sammy
 * Create: 2024/8/26
 * Version: 1.0
 */

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * 初始化系統中的管理員賬戶。
     * 若不存在 "admin" 帳號，則創建一個預設密碼為 "admin" 的管理員帳號並分配 "ROLE_ADMIN" 角色。
     */
    public void createAdminUser() {
        Optional<User> existingUser = userRepository.findByUsername("admin");
        if (!existingUser.isPresent()) {
            System.out.println("Admin user not found, creating...");
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            Role adminRole = roleService.findOrCreateRole("ROLE_ADMIN");
            admin.setRole(adminRole);
            userRepository.save(admin);
            System.out.println("Admin user created successfully!");
        } else {
            System.out.println("Admin user already exists.");
        }
    }

    /**
     * 用於註冊新用戶，驗證用戶名唯一性並分配默認 "ROLE_USER" 角色。
     *
     * @param userRequest 用戶註冊請求，包含用戶名、電子郵件和密碼。
     * @return 註冊後保存的用戶信息。
     */
    public User registerUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            System.out.println("Username already exists: " + userRequest.getUsername());
            throw new IllegalArgumentException("Username is already taken.");
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() ->
                new RuntimeException("Default role not found.")
        );
        user.setRole(userRole);
        return userRepository.save(user);
    }

    /**
     * 驗證用戶身份，生成並返回 JWT 標記。
     *
     * @param loginRequest 包含用戶名和密碼的登入請求。
     * @return 驗證成功的 JWT 標記。
     */
    public String authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password.");
        }

        return jwtTokenProvider.createToken(user.getUsername(), user.getRole());
    }

    /**
     * 根據用戶 ID 查找用戶信息。
     *
     * @param userId 用戶 ID
     * @return 對應的用戶實體，若不存在則返回 null。
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * 根據用戶名查找用戶。
     *
     * @param username 用戶名
     * @return 包含用戶信息的 Optional 對象。
     */
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 設定用戶的角色。
     *
     * @param userId   用戶 ID
     * @param roleName 角色名稱
     * @return 更新角色後的用戶資訊。
     */
    public User setUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found."));

        user.setRole(role);

        return userRepository.save(user);
    }

    /**
     * 根據用戶名查找用戶，若找不到則拋出 UsernameNotFoundException。
     *
     * @param username 用戶名
     * @return 對應的用戶實體。
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}








