package com.example.ticket_platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticket_platform.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByNome(String nome);
}
