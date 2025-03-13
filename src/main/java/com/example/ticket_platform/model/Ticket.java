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
import jakarta.validation.constraints.NotNull;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User operatore;

    private LocalDate dataCreazione;

    @Lob
    @NotBlank(message = "La descrizione non può essere vuota.")
    private String descrizione;

    @Lob
    private String note;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

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

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOperatoreUsername() {
        return this.operatore.getUsername();
    }

    public String getStatusName() {
        return this.status.getStatus().toString();
    }

}
