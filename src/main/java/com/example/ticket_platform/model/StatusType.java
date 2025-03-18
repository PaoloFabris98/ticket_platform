package com.example.ticket_platform.model;

public enum StatusType {
    APERTO,
    IN_CORSO,
    CHIUSO;

    public String getName() {
        return this.name();
    }

    public String getLabel() {
        return this.name();
    }
}
