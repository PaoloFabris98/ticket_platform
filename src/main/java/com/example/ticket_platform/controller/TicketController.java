package com.example.ticket_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.ticket_platform.model.StatusType;
import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.repository.StatusRepository;
import com.example.ticket_platform.repository.TicketRepository;
import com.example.ticket_platform.service.StatusService;
import com.example.ticket_platform.service.TicketService;
import com.example.ticket_platform.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TicketController {

    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    TicketService ticketService;
    @Autowired
    UserService userService;
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    StatusService statusService;

    TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/ticket/{id}")
    public String getMethodName(@PathVariable Integer id, Model model) {
        Ticket ticket = ticketService.getTicketById(id);
        model.addAttribute("ticket", ticket);
        return "ticket/ticket";
    }

    @GetMapping("/editTicket/{id}")
    public String editTicket(@PathVariable Integer id, Model model) {
        Ticket ticket = ticketService.getTicketById(id);
        List<User> users = userService.getAll();
        List<StatusType> statusTypes = statusService.getAllStatusTypes();
        model.addAttribute("ticket", ticket);
        model.addAttribute("users", users);
        model.addAttribute("status", statusTypes);
        return "ticket/edit";
    }

    @PostMapping("/editTicket/{id}")
    public String editTicket(@Valid @ModelAttribute("ticket") Ticket formTicket,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ticket/edit";
        }
        ticketService.saveTicket(formTicket);

        redirectAttributes.addFlashAttribute("message", "Il ticket è stato modificato con successo");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");
        return "index/index";
    }

}
