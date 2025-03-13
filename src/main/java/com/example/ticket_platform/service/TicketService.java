package com.example.ticket_platform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.repository.TicketRepository;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }
}
