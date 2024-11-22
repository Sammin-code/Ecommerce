package com.sprbt01.ecommerce.repository;

import com.sprbt01.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Class Name: UserRepository
 * Package: com.sprbt01.ecommerce.repository
 * Description:提供對 User 實體的數據訪問，包括根據用戶名查找用戶和檢查用戶名是否存在。
 * author: Sammy
 * Create: 2024/9/1 下午 02:22
 * Version: 1.0
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 根據用戶名查找用戶。
     * @param username 用戶名
     * @return 返回包含指定用戶名的 User 的 Optional
     */
    Optional<User> findByUsername(String username);

    /**
     * 檢查指定用戶名是否已存在。
     * @param username 用戶名
     * @return 如果用戶名存在則返回 true，否則返回 false
     */
    boolean existsByUsername(String username);
}

