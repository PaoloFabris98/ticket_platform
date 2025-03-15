package com.example.ticket_platform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.repository.StatusRepository;
import com.example.ticket_platform.repository.TicketRepository;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    StatusRepository statusRepository;

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getTicketsByUserId(Integer userId) {
        return ticketRepository.findByOperatoreId(userId);
    }

    public Ticket getTicketById(Integer id) {
        return ticketRepository.findById(id).get();
    }

    public void updateTicket(Ticket ticket) {
        statusRepository.save(ticket.getStatus());
        ticketRepository.save(ticket);
    }

    public void saveTicket(Ticket ticket) {
        statusRepository.save(ticket.getStatus());
        ticketRepository.save(ticket);
    }
}
