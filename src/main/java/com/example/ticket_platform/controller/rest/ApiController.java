package com.example.ticket_platform.controller.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.repository.ApiUserRepository;
import com.example.ticket_platform.repository.TicketRepository;
import com.example.ticket_platform.repository.UserRepository;
import com.example.ticket_platform.service.StatusService;
import com.example.ticket_platform.service.TicketService;
import com.example.ticket_platform.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@CrossOrigin
public class ApiController {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private ApiUserRepository apiUserRepository;
    @Autowired
    private StatusService statusService;
    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/API/get_all_tickets/{authKey}/{all_ticket_auth_key}")
    public ResponseEntity<?> getAllTickets(@PathVariable String authKey, @PathVariable String all_ticket_auth_key) {
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

        for (Object principal : allPrincipals) {
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;

                User user = userRepository.findByUsername(userDetails.getUsername()).get();

                if (user != null &&
                        authKey.equals(user.getApiAuthKey()) &&
                        all_ticket_auth_key.equals(user.getAllTicketAuthKey())) {

                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                    if (!sessions.isEmpty()) {
                        return new ResponseEntity<>(ticketService.findAll(), HttpStatus.OK);
                    }
                }
            }
        }

        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

}
