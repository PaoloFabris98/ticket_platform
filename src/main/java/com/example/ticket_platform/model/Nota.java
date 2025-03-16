package com.example.ticket_platform.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Nota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "autore_id")
    private User autore;

    @NotNull(message = "La data non può essere vuota")
    private LocalDate dataCreazione;

    @Lob
    private String nota;

    @ManyToOne
    private Ticket ticket;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getAutore() {
        return this.autore;
    }

    public void setAutore(User autore) {
        this.autore = autore;
    }

    public String getNota() {
        return this.nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public LocalDate getDataCreazione() {
        return this.dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.dataCreazione.format(formatter);
    }

}
