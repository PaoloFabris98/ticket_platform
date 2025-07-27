package com.example.ticket_platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticket_platform.model.Magazzino;
import java.util.List;

public interface MagazzinoRepository extends JpaRepository<Magazzino, Integer> {
    Optional<Magazzino> findByName(String name);
}
