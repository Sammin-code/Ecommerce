package com.sprbt01.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class Name: HomeController
 * Package: com.sprbt01.ecommerce.controller
 * Description:
 * author: Sammy
 * Create: 2024/8/29 上午 08:30
 * Version: 1.0
 */
@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping("/home")
    public String getWelcomeMessage() {
        return "Welcome to the Home Page!";
    }
}
