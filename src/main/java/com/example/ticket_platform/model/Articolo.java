package com.example.ticket_platform.model;

import java.io.Serializable;
import java.util.List;

import com.example.ticket_platform.model.dto.ArticoloDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Articolo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String name;
    @Lob
    private String descrizione;

    @ManyToOne
    @JoinColumn(name = "magazzino_id")
    private Magazzino magazzino;

    @OneToMany(mappedBy = "articolo", cascade = CascadeType.ALL)
    private List<Codice> codici;

    private Integer quantità;

    private Integer quantitàMinima = 0;
    private Integer quantitàMassima = 0;

    public Articolo() {
    }

    public Articolo(ArticoloDTO articoloDTO) {
        this.setName(articoloDTO.getName());
        this.setDescrizione(articoloDTO.getDescrizione());
    }

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

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public List<Codice> getCodici() {
        return this.codici;
    }

    public void setCodici(List<Codice> codici) {
        this.codici = codici;
    }

    public Magazzino getMagazzino() {
        return this.magazzino;
    }

    public void setMagazzino(Magazzino magazzino) {
        this.magazzino = magazzino;
    }

    public Integer getQuantità() {
        return this.quantità;
    }

    public void setQuantità(Integer quantità) {
        this.quantità = quantità;
    }

    public void addQuantity(Integer quantity) {
        this.quantità += quantity;
    }

    public void subtractQuantity(Integer quantity) {
        this.quantità -= quantity;
    }

    public Integer getQuantitàMinima() {
        return this.quantitàMinima;
    }

    public void setQuantitàMinima(Integer quantitàMinima) {
        this.quantitàMinima = quantitàMinima;
    }

    public Integer getQuantitàMassima() {
        return this.quantitàMassima;
    }

    public void setQuantitàMassima(Integer quantitàMassima) {
        this.quantitàMassima = quantitàMassima;
    }

}
