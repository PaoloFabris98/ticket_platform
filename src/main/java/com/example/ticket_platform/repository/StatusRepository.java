package com.example.ticket_platform.repository;

import com.example.ticket_platform.model.Status;
import com.example.ticket_platform.model.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    Optional<Status> findByStatus(StatusType status);
}