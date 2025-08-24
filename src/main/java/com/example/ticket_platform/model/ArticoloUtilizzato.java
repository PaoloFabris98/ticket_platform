package com.example.ticket_platform.model;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "articoloUtilizzato")
public class ArticoloUtilizzato implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "articolo_id")
    private Articolo articolo;

    private Integer quantità;

    private LocalDate dataUtilizzo;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Articolo getArticolo() {
        return this.articolo;
    }

    public void setArticolo(Articolo articolo) {
        this.articolo = articolo;
    }

    public Integer getQuantità() {
        return this.quantità;
    }

    public void setQuantità(Integer quantità) {
        this.quantità = quantità;
    }

    public LocalDate getDataUtilizzo() {
        return this.dataUtilizzo;
    }

    public void setDataUtilizzo(LocalDate dataUtilizzo) {
        this.dataUtilizzo = dataUtilizzo;
    }

}
