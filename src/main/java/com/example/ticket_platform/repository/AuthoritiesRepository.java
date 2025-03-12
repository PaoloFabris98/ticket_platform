package com.example.ticket_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ticket_platform.model.Authorities;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Integer> {
    List<Authorities> findByUsername(String username);
}
