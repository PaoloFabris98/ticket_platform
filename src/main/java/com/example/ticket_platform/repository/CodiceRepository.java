package com.example.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticket_platform.model.Codice;

public interface CodiceRepository extends JpaRepository<Codice, Integer> {

}
