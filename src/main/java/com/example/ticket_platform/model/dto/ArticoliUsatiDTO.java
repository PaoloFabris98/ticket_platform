package com.example.ticket_platform.model.dto;

import com.example.ticket_platform.model.Articolo;

public class ArticoliUsatiDTO {
    private Articolo articolo;
    private Integer quantità;
    private String codice;

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

}
