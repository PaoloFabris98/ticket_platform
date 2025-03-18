package com.example.ticket_platform.model;

public enum UserStatusType {
    DISPONIBILE,
    NON_DISPONIBILE;

    public String getName() {
        return this.name();
    }

    public String getLabel() {
        return this.name();
    }
}
