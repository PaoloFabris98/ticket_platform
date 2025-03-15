package com.example.ticket_platform.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.service.TicketService;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UtilityFunctions utilityFunctions;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/index")
    public String index(Model model, Principal principal) {
        if (utilityFunctions.isAdmin(utilityFunctions.currentUser(principal))) {
            List<Ticket> allTickets = ticketService.findAll();
            model.addAttribute("tickets", allTickets);
        } else {
            List<Ticket> userTickets = ticketService
                    .getTicketsByUserId(utilityFunctions.currentUser(principal).getId());
            model.addAttribute("tickets", userTickets);
        }

        return "index/index";
    }

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

}
