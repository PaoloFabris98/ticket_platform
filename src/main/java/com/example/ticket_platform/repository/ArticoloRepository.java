package com.example.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticket_platform.model.Articolo;

public interface ArticoloRepository extends JpaRepository<Articolo, Integer> {

}
