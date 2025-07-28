package com.example.ticket_platform.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ArticoloUtilizzato implements Serializable {
    @Id
    private Integer id;

    @ManyToOne
    private Ticket ticket;

    private Articolo articolo;

    private List<Codice> codici;

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

    public List<Codice> getCodici() {
        return this.codici;
    }

    public void setCodici(List<Codice> codici) {
        this.codici = codici;
    }

}
