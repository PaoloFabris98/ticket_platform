package com.example.ticket_platform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticket_platform.model.User;
import com.example.ticket_platform.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    };

    public void addUser(User user) {
        userRepository.save(user);
    }
}
