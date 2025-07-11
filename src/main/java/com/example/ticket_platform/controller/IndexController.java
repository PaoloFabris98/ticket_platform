package com.example.ticket_platform.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.dto.TempUser;
import com.example.ticket_platform.repository.UserRepository;
import com.example.ticket_platform.security.CustomJdbcUserDetailsManager;
import com.example.ticket_platform.service.TicketService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UtilityFunctions utilityFunctions;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomJdbcUserDetailsManager customJdbcUserDetailsManager;

    @ModelAttribute("currentUser")
    public String getCurrentUser(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return utilityFunctions.currentUser(principal).getUsername();
        }
        return "redirect:/login";
    }

    @ModelAttribute("currentUserObj")
    public TempUser getCurrentUserObj(Principal principal) {
        User user = utilityFunctions.currentUser(principal);

        if (user == null) {
            return null;
        }

        TempUser tempUser = new TempUser();
        tempUser.setId(user.getId());
        tempUser.setUsername(user.getUsername());
        tempUser.setRole(user.getRole());
        tempUser.setUserStatus(user.getUserStatus());
        tempUser.setApiAuthKey(user.getApiAuthKey());
        tempUser.setAllTicketAuthKey(user.getAllTicketAuthKey());
        return tempUser;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/index")
    public String index(Model model, Principal principal) {

        User user = userRepository.findByUsername(principal.getName()).get();
        LocalDateTime now = LocalDateTime.now();

        if (user.getApiAuthKeyLastUpdated() != null &&
                user.getApiAuthKeyLastUpdated().isBefore(now.minusHours(1))) {

            user.setApiAuthKey(utilityFunctions.authKeyGenerator(30));
            user.setAllTicketAuthKey(utilityFunctions.authKeyGenerator(30));
            user.setApiAuthKeyLastUpdated(now);

            customJdbcUserDetailsManager.updateUserApiKey(user, principal);
        }

        if (utilityFunctions.isAdmin(utilityFunctions.currentUser(principal))) {
            List<Ticket> allTicketsNotFiltered = ticketService.findAll();
            List<Ticket> allTicketFiltered = new ArrayList<>();
            for (Ticket ticket : allTicketsNotFiltered) {
                if (ticket.getStatus().getStatus().equals("CHIUSO")) {
                    if (ticket.getDataChiusura() != null &&
                            ticket.getDataChiusura().isEqual(now.minusDays(1).toLocalDate())) {
                        continue;
                    }
                    allTicketFiltered.add(ticket);
                } else {
                    allTicketFiltered.add(ticket);
                }

            }
            model.addAttribute("tickets", allTicketFiltered);
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
