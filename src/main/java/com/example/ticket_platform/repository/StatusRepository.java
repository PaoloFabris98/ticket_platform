package com.example.ticket_platform.repository;

import com.example.ticket_platform.model.Status;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    public Status findByStatus(String status);

}