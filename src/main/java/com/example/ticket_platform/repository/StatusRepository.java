package com.example.ticket_platform.repository;

import com.example.ticket_platform.model.Status;
import com.example.ticket_platform.model.StatusType;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    public Status findByStatus(StatusType status);

}