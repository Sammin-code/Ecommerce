package com.sprbt01.ecommerce;

import com.sprbt01.ecommerce.service.RoleService;
import com.sprbt01.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    public CommandLineRunner setupDefaultAdmin(UserService userService, RoleService roleService) {
        return args -> {
            roleService.initializeRoles();

            userService.createAdminUser();
        };
    }

}

