package com.example.ticket_platform.component;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ticket_platform.model.Authorities;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.service.AuthoritiesService;
import com.example.ticket_platform.service.StatusService;
import com.example.ticket_platform.service.TicketService;
import com.example.ticket_platform.service.UserService;

@Component
public class UtilityFunctions {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthoritiesService authoritiesService;

    @Autowired
    private StatusService statusService;

    public User currentUser(Principal principal) {
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
}
