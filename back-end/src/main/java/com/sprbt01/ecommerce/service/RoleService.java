package com.sprbt01.ecommerce.service;

import com.sprbt01.ecommerce.model.Role;
import com.sprbt01.ecommerce.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Class Name: RoleService
 * Package: com.sprbt01.ecommerce.service
 * Description: 負責處理角色相關的業務邏輯，包括查找或創建角色，以及初始化系統角色。
 * author: Sammy
 * Create: 2024/8/26
 * Version: 1.0
 */

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    /**
     * 查找指定名稱的角色，若不存在則創建該角色
     * @param roleName 角色名稱
     * @return 查找到的角色或新創建的角色
     */
    public Role findOrCreateRole(String roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        if (role.isEmpty()) {
            Role newRole = new Role();
            newRole.setName(roleName);
            role = Optional.of(roleRepository.save(newRole));
        }
        return role.get();
    }
    /**
     * 初始化系統角色，確保 "ROLE_ADMIN" 和 "ROLE_USER" 存在於資料庫中
     * 若角色不存在則自動創建
     */
    public void initializeRoles() {
        findOrCreateRole("ROLE_ADMIN");
        findOrCreateRole("ROLE_USER");
    }
}


