package com.example.ticket_platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticket_platform.repository.CodiceRepository;

@Service
public class CodiceService {
    @Autowired
    private CodiceRepository codiceRepository;
}
