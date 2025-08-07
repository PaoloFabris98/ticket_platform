package com.example.ticket_platform.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    @JsonBackReference
    private User operatore;

    @NotNull(message = "La data non può essere vuota")
    private LocalDate dataCreazione;

    private LocalDate dataChiusura;

    @OneToMany(mappedBy = "ticket", orphanRemoval = true)
    @JsonManagedReference
    private List<Allegato> file;

    @Lob
    @NotBlank(message = "La descrizione non può essere vuota.")
    private String descrizione;

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
    private Cliente cliente;

    @OneToMany(mappedBy = "ticket", orphanRemoval = true)
    @JsonManagedReference
    private List<Nota> note;

    @OneToMany(mappedBy = "ticket")
    private List<ArticoloUtilizzato> articoliUtilizzati;

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

    public List<Nota> getNote() {
        return this.note;
    }

    public void setNote(List<Nota> note) {
        this.note = note;
    }

    public LocalDate getDataChiusura() {
        return this.dataChiusura;
    }

    public void setDataChiusura(LocalDate dataChiusura) {
        this.dataChiusura = dataChiusura;
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

    public String getOperatoreUsername() {
        return this.operatore.getUsername();
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.dataCreazione.format(formatter);
    }

    public String getFormattedClosedDate() {
        if (this.dataChiusura == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.dataChiusura.format(formatter);
    }

    public String getStatusName() {
        return this.status.getStatus().toString();
    }

    public void addFile(Allegato file) {
        this.file.add(file);
    }

    public List<Allegato> getImgs() {
        return this.file;
    }

    public void setImgs(List<Allegato> file) {
        this.file = file;
    }

    public List<Allegato> getFile() {
        return this.file;
    }

    public void setFile(List<Allegato> file) {
        this.file = file;
    }

    public List<ArticoloUtilizzato> getArticoliUtilizzati() {
        return this.articoliUtilizzati;
    }

    public void setArticoliUtilizzati(List<ArticoloUtilizzato> articoliUtilizzati) {
        this.articoliUtilizzati = articoliUtilizzati;
    }

    public void addArticolo(ArticoloUtilizzato articolo) {
        this.articoliUtilizzati.add(articolo);
    }

}
