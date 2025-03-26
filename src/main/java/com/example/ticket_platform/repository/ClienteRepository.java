package com.example.ticket_platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticket_platform.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByNome(String nome);

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByCellulare(String cellulare);

    boolean existsByNome(String nome);

    boolean existsByEmail(String email);

    boolean existsByCellulare(String cellulare);

    boolean existsByNomeAndIdNot(String nome, Integer id);

    boolean existsByEmailAndIdNot(String email, Integer id);

    boolean existsByCellulareAndIdNot(String cellulare, Integer id);
}
