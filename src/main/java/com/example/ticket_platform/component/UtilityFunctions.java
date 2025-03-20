package com.example.ticket_platform.component;

import java.security.Principal;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ticket_platform.model.Authorities;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.service.AuthoritiesService;

import com.example.ticket_platform.service.UserService;

@Component
public class UtilityFunctions {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthoritiesService authoritiesService;

    public User currentUser(Principal principal) {
        if (principal == null)
            return null;
        return userService.findByUsernameUser(principal.getName());
    }

    public Boolean isAdmin(User currentUser) {
        Authorities authorities = authoritiesService.findByUsername(currentUser);
        return "ADMIN".equals(authorities.getAuthority());
    }

    public Boolean isUser(User currentUser) {
        Authorities authorities = authoritiesService.findByUsername(currentUser);
        return "USER".equals(authorities.getAuthority());
    }

    public String authKeyGenerator(int i) {
        String allowedChar = "ABCDEFGHIJKLMNOPQRSTUWXYZabcdefghijklmnopqrstuwxyz1234567890";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();
        while (randomString.length() < i) {
            int index = random.nextInt(allowedChar.length());
            randomString.append(allowedChar.charAt(index));
        }
        String newAuthKey = randomString.toString();
        return newAuthKey;
    }
}
