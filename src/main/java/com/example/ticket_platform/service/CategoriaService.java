package com.example.ticket_platform.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticket_platform.model.CategoriaTicketType;
import com.example.ticket_platform.repository.CategoriaRepository;

@Service
public class CategoriaService {
    @Autowired
    CategoriaRepository categoriaRepository;

    public List<CategoriaTicketType> getAllCategoriaStatusTypes() {
        List<CategoriaTicketType> categoriaTicketTypesList = Arrays.asList(CategoriaTicketType.values());
        return categoriaTicketTypesList;
    }
}
