package com.example.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticket_platform.model.ArticoloUtilizzato;
import com.example.ticket_platform.model.Ticket;

import java.util.List;

public interface ArticoloUtilizzatoRepository extends JpaRepository<ArticoloUtilizzato, Integer> {
    List<ArticoloUtilizzato> findByTicket(Ticket ticket);
}
