package com.example.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticket_platform.model.Magazzino;

public interface MagazzinoRepository extends JpaRepository<Magazzino, Integer> {

}
