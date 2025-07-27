package com.example.ticket_platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticket_platform.model.Articolo;
import com.example.ticket_platform.repository.ArticoloRepository;

@Service
public class ArticoloService {
    @Autowired
    private ArticoloRepository articoloRepository;

}
