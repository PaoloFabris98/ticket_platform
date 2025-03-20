package com.example.ticket_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticket_platform.model.Nota;
import com.example.ticket_platform.model.User;

public interface NotaRepository extends JpaRepository<Nota, Integer> {
    List<Nota> findByAutore(User autore);
}
