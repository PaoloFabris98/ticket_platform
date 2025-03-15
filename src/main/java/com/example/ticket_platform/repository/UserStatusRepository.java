package com.example.ticket_platform.repository;

import com.example.ticket_platform.model.UserStatus;
import com.example.ticket_platform.model.UserStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Integer> {
    UserStatus findByUserStatusType(UserStatusType userStatusType);

}