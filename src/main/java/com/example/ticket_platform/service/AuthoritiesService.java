package com.example.ticket_platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ticket_platform.model.Authorities;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.repository.AuthoritiesRepository;

@Service
public class AuthoritiesService {

    @Autowired
    AuthoritiesRepository authoritiesRepository;

    public Authorities findByUsername(User user) {
        return authoritiesRepository.findByUsername(user.getUsername()).getFirst();
    }
}
