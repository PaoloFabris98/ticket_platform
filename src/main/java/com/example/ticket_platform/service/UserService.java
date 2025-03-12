package com.example.ticket_platform.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticket_platform.model.User;
import com.example.ticket_platform.repository.AuthoritiesRepository;
import com.example.ticket_platform.repository.UserRepository;
import com.example.ticket_platform.security.CustomJdbcUserDetailsManager;
import com.example.ticket_platform.security.DatabaseUserDetails;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    };
}
