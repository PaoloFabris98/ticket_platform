package com.example.ticket_platform.model.dto;

import com.example.ticket_platform.model.Articolo;

public class ArticoliUsatiDTO {
    private Integer id;
    private Articolo articolo;
    private Integer quantità;
    private String codice;
    private Integer quantitàUsata;

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

    public String getCodice() {
        return this.codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public Integer getQuantitàUsata() {
        return this.quantitàUsata;
    }

    public void setQuantitàUsata(Integer quantitàUsata) {
        this.quantitàUsata = quantitàUsata;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
