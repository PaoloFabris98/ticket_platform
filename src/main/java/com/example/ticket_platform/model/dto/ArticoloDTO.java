package com.example.ticket_platform.model.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;

public class ArticoloDTO {

    @NotBlank
    private String name;
    @Lob
    private String descrizione;

    private String Codice;

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

    public String getCodice() {
        return this.Codice;
    }

    public void setCodice(String Codice) {
        this.Codice = Codice;
    }

}
