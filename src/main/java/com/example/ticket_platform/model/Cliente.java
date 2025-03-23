package com.example.ticket_platform.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Il campo del nome non può essere vuoto.")
    @Column(unique = true)
    private String nome;

    @NotBlank(message = "Il campo della mail non può essere vuoto.")
    @Column(unique = true)
    private String mail;

    @NotBlank(message = "Il numero di telefono non può essere vuoto.")
    private String cellulare;

    @OneToMany(mappedBy = "cliente", orphanRemoval = true)
    @JsonBackReference
    private List<Ticket> tickets;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCellulare() {
        return this.cellulare;
    }

    public void setCellulare(String cellulare) {
        this.cellulare = cellulare;
    }

    public List<Ticket> getTickets() {
        return this.tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

}
