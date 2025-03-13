package com.example.ticket_platform.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ticket_platform.model.Authorities;
import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.repository.TicketRepository;
import com.example.ticket_platform.service.AuthoritiesService;
import com.example.ticket_platform.service.TicketService;
import com.example.ticket_platform.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthoritiesService authoritiesService;

    @GetMapping("/")
    public String index(Model model, Principal principal) {

        User currentUser = userService.findByUsernameUser(principal.getName());
        Authorities authorities = authoritiesService.findByUsername(currentUser);

        if (authorities.getAuthority().equals("ADMIN")) {
            List<Ticket> allTickets = ticketService.findAll();
            model.addAttribute("tickets", allTickets);
        } else {
            List<Ticket> userTickets = ticketService.getTicketsByUserId(currentUser.getId());
            model.addAttribute("tickets", userTickets);
        }

        return "index/index";
    }

    @GetMapping("/access_denied_not_autenticated")
    public String accessDeniedNotAutenticated() {
        return "index/accessDeniedNotAutenticated";
    }

    @GetMapping("/access_denied_autenticated")
    public String accessDeniedAutenticated() {
        return "index/accessDeniedAutenticated";
    }

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

}
