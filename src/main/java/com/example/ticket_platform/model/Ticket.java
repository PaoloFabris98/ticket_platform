package com.example.ticket_platform.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "L'autore non può essere vuoto.")
    private User autore;

    @NotBlank(message = "La data di creazione non può essere vuota.")
    private LocalDate dataCreazione;

    @Lob
    @NotBlank(message = "La descrizione non può essere vuota.")
    private String descrizione;
}
