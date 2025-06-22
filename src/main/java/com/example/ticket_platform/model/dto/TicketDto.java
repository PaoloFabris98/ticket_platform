package com.example.ticket_platform.model.dto;

import java.time.LocalDate;
import java.util.List;

import com.example.ticket_platform.model.Categoria;
import com.example.ticket_platform.model.Cliente;
import com.example.ticket_platform.model.Nota;
import com.example.ticket_platform.model.Status;
import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

public class TicketDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    @JsonBackReference
    private User operatore;

    @NotNull(message = "La data non può essere vuota")
    private LocalDate dataCreazione;

    private LocalDate dataChiusura;

    public TicketDto() {
    }

    public TicketDto(Ticket ticket) {
        setId(ticket.getId());
        setOperatore(ticket.getOperatore());
        setDataCreazione(ticket.getDataCreazione());
        setDataChiusura(ticket.getDataChiusura());
        setStatus(ticket.getStatus());
        setCategoria(ticket.getCategoria());
        setCliente(ticket.getCliente());
    }

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    @JsonBackReference
    private Status status;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    @JsonBackReference
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonBackReference
    private Cliente cliente;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getOperatore() {
        return this.operatore;
    }

    public void setOperatore(User operatore) {
        this.operatore = operatore;
    }

    public LocalDate getDataCreazione() {
        return this.dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public LocalDate getDataChiusura() {
        return this.dataChiusura;
    }

    public void setDataChiusura(LocalDate dataChiusura) {
        this.dataChiusura = dataChiusura;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

}
