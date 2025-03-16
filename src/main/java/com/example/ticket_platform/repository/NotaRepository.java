package com.example.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticket_platform.model.Nota;

public interface NotaRepository extends JpaRepository<Nota, Integer> {

}
