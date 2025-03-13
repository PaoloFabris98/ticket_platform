package com.example.ticket_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ticket_platform.model.Ticket;

import com.example.ticket_platform.repository.TicketRepository;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    TicketRepository ticketRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Ticket> tickets = ticketRepository.findAll();
        model.addAttribute("tickets", tickets);

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
