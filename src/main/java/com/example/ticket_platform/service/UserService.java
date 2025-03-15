package com.example.ticket_platform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public User findByUsernameUser(String name) {
        return userRepository.findByUsername(name).get();
    }

    public User findUserById(Integer Id) {
        return userRepository.findById(Id).get();
    }

    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }
}
