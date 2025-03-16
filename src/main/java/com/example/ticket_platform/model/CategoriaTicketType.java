package com.example.ticket_platform.model;

public enum CategoriaTicketType {
    ASSISTENZA,
    MANUTENZIONE,
    AMMINISTRAZIONE,
    TECNICA;

    public String getLabel() {
        return this.name();
    }
}
