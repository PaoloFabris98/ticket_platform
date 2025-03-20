package com.example.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ticket_platform.model.ApiUser;
import java.util.List;

public interface ApiUserRepository extends JpaRepository<ApiUser, Integer> {
    List<ApiUser> findByUsername(String username);
}
