package com.example.ticket_platform.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticket_platform.model.Cliente;
import com.example.ticket_platform.repository.ClienteRepository;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public Optional<Cliente> findById(Integer id) {
        return clienteRepository.findById(id);
    }
}
