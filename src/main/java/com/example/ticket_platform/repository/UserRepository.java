package com.example.ticket_platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.UserStatus;
import com.example.ticket_platform.model.UserStatusType;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Integer id);

    void deleteByUsername(String username);

    public UserStatus findByUserStatus(UserStatusType status);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String nome);

    boolean existsByEmail(String email);

}
