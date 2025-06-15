package com.example.ticket_platform.repository;

import com.example.ticket_platform.model.File;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Integer> {

}
