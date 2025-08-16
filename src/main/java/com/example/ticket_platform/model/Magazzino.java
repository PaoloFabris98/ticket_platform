package com.example.ticket_platform.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Magazzino implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "magazzino", cascade = CascadeType.ALL)
    private List<Articolo> articoli;

    @OneToOne(optional = true)
    @JoinColumn(name = "proprietario_id", referencedColumnName = "id")
    private User proprietario;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Articolo> getArticoli() {
        return this.articoli;
    }

    public void setArticoli(List<Articolo> articoli) {
        this.articoli = articoli;
    }

    public Articolo getArticoloById(Integer id) {
        for (Articolo articolo : this.getArticoli()) {
            if (articolo.getId() == id) {
                return articolo;
            }
        }
        return null;
    }

    public Articolo getArticoloByName(String name) {
        for (Articolo articolo : this.getArticoli()) {
            if (articolo.getName().equals(name)) {
                return articolo;
            }
        }
        return null;
    }

    public User getProprietario() {
        return this.proprietario;
    }

    public void setProprietario(User proprietario) {
        this.proprietario = proprietario;
    }

}
