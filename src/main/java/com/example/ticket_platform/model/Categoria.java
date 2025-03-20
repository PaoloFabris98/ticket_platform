package com.example.ticket_platform.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(unique = false, nullable = false)
    private CategoriaTicketType nome;

    @OneToMany(mappedBy = "categoria")
    @JsonBackReference
    private List<Ticket> tickets;

    public Categoria() {
    }

    public Categoria(CategoriaTicketType nome) {
        this.nome = nome;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CategoriaTicketType getNome() {
        return this.nome;
    }

    public void setNome(CategoriaTicketType nome) {
        this.nome = nome;
    }

    public List<Ticket> getTickets() {
        return this.tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public String getCategoriaName() {
        return nome.getLabel();
    }
}
