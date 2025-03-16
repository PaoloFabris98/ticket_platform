package com.example.ticket_platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticket_platform.model.Nota;
import com.example.ticket_platform.repository.NotaRepository;

@Service
public class NotaService {
    @Autowired
    NotaRepository notaRepository;

    public void save(Nota nota) {
        notaRepository.save(nota);
    }
}
