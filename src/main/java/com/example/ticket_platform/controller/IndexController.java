package com.example.ticket_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ticket_platform.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        return "index/index";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "index/accessDenied";
    }

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

}
