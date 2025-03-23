package com.example.ticket_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticket_platform.model.Cliente;
import com.example.ticket_platform.model.Status;
import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.model.User;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    long countByOperatore(User operatore);

    List<Ticket> findByOperatoreId(Integer userId);

    List<Ticket> findByStatus(Status status);

    List<Ticket> findByCliente(Cliente cliente);

}