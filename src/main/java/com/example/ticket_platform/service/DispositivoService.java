package com.example.ticket_platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticket_platform.repository.DispositivoRepository;

@Service
public class DispositivoService {
    @Autowired
    private DispositivoRepository dispositivoRepository;

}
