package com.example.ticket_platform.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + name));
    }

    public User findUserById(Integer Id) {
        return userRepository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("Utente con ID " + Id + " non trovato"));
    }

    public List<User> finaAllByStatus() {
        List<User> users = getAll();
        List<User> aviableUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getUserStatus().getStatusName().equals("DISPONIBILE")) {

                aviableUsers.add(user);
            }
        }
        return aviableUsers;
    }

    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }
}
