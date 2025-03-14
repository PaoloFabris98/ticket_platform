package com.example.ticket_platform.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Status implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(unique = false, nullable = false)
    private StatusType status;

    @OneToMany(mappedBy = "status")
    private List<Ticket> tickets;

    public Status() {
    }

    public Status(String status) {
        try {
            this.status = StatusType.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
