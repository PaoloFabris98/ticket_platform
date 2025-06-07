package com.example.ticket_platform.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticket_platform.model.Categoria;
import com.example.ticket_platform.repository.CategoriaRepository;

@Service
public class CategoriaService {
    @Autowired
    CategoriaRepository categoriaRepository;

    public List<Categoria> getAllCategoriaStatusTypes() {
        List<Categoria> categorie = categoriaRepository.findAll();
        return categorie;
    }
}
