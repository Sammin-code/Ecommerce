package com.sprbt01.ecommerce.service;

import com.sprbt01.ecommerce.model.Role;
import com.sprbt01.ecommerce.model.User;
import com.sprbt01.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class Name: CustomUserDetailsService
 * Package: com.sprbt01.ecommerce.service
 * Description:實現 Spring Security 的 UserDetailsService，負責根據用戶名仔入用戶信息，並將用戶的角色轉換為授權信息。
 * author: Sammy
 * Create: 2024/9/1
 * Version: 1.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 根據用戶名載入用戶信息。
     * @param username 用戶名
     * @return 用戶的詳細信息，包括用戶名、密碼和授權信息
     * @throws UsernameNotFoundException 當用戶不存在時拋出
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRole())
        );
    }
    /**
     * 將用戶角色轉換為授權信息。
     * @param role 用戶的角色
     * @return 角色對應的授權信息列表
     */
    private List<GrantedAuthority> mapRolesToAuthorities(Role role) {
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }


}
