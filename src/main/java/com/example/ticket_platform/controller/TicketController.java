package com.example.ticket_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.ticket_platform.service.TicketService;

@Controller
public class TicketController {
    @Autowired
    TicketService ticketService;

}
