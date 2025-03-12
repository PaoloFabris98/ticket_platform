package com.example.ticket_platform.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ticket_platform.model.User;

import javax.sql.DataSource;
import com.example.ticket_platform.repository.AuthoritiesRepository;
import com.example.ticket_platform.repository.UserRepository;
import com.example.ticket_platform.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    private final DataSource dataSource;

    private final AuthoritiesRepository authoritiesRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    IndexController(AuthoritiesRepository authoritiesRepository, DataSource dataSource) {
        this.authoritiesRepository = authoritiesRepository;
        this.dataSource = dataSource;
    }

    @GetMapping("/")
    public String index(Model model) {

        if (userRepository.findAll().size() == 0) {
            List<User> users = userService.getAll();
            model.addAttribute(users);
        } else {
            List<User> users = userService.getAll();
            model.addAttribute(users);
        }

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
