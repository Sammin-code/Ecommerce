package com.sprbt01.ecommerce.repository;
import com.sprbt01.ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Class Name: ProductRepository
 * Package: com.sprbt01.ecommerce.repository
 * Description:提供對 Role 實體的數據訪問，包括根據角色名稱查找角色。
 * author: Sammy
 * Create: 2024/8/26
 * Version: 1.0
 */

public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * 根據角色名稱查找角色。
     * @param name 角色名稱
     * @return 返回包含指定名稱的 Role 的 Optional
     */
    Optional<Role> findByName(String name);
}
