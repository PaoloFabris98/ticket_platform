package com.example.ticket_platform.repository;

import com.example.ticket_platform.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    Categoria findByNome(String nome);
}