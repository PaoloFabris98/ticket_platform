package com.example.ticket_platform.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.example.ticket_platform.model.ApiUser;
import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.repository.ApiUserRepository;
import com.example.ticket_platform.service.TicketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin
public class ApiController {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private ApiUserRepository apiUserRepository;

    @GetMapping("/API/get_all_tickets/{authKey}/{username}/{password}")
    public ResponseEntity<?> getAllTickets(@PathVariable String authKey,
            @PathVariable String username,
            @PathVariable String password) {

        if (apiUserRepository.findByUsername(username).get(0) == null) {
            return new ResponseEntity<String>("Invalid username.", HttpStatus.UNAUTHORIZED);
        }
        if (!(apiUserRepository.findByUsername(username).get(0).getPassword().equals(password))) {
            return new ResponseEntity<String>("Invalid password.", HttpStatus.UNAUTHORIZED);
        }
        if (!(apiUserRepository.findByUsername(username).get(0).getAuthKey().equals(authKey))) {
            return new ResponseEntity<String>("Invalid authKey.", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(ticketService.findAll(), HttpStatus.OK);
    }

}
