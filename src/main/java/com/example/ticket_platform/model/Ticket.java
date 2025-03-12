package com.example.ticket_platform.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "L'autore non può essere vuoto.")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User autore;

    @NotBlank(message = "La data di creazione non può essere vuota.")
    private LocalDate dataCreazione;

    @Lob
    @NotBlank(message = "La descrizione non può essere vuota.")
    private String descrizione;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return this.autore;
    }

    public void setUser(User user) {
        this.autore = user;
    }

    public LocalDate getDataCreazione() {
        return this.dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

}
