package com.example.ticket_platform.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ticket_platform.model.Authorities;
import com.example.ticket_platform.model.AuthoritiesType;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.repository.AuthoritiesRepository;

@Service
public class AuthoritiesService {

    @Autowired
    AuthoritiesRepository authoritiesRepository;

    public Authorities findByUsername(User user) {
        return authoritiesRepository.findByUsername(user.getUsername()).getFirst();
    }

    public Authorities findByUsername(String user) {
        return authoritiesRepository.findByUsername(user).getFirst();
    }

    public List<AuthoritiesType> getAllAuthoritiesTypes() {
        List<AuthoritiesType> AuthoritiesTypeTypesList = Arrays.asList(AuthoritiesType.values());
        return AuthoritiesTypeTypesList;
    }

}
