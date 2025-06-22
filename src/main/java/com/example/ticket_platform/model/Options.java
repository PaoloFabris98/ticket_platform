package com.example.ticket_platform.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Options {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer maxOption = 1;
    private String ticketUploadDir;
    private String manualsUploadDir;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaxOption() {
        return this.maxOption;
    }

    public void setMaxOption(Integer maxOption) {
        this.maxOption = maxOption;
    }

    public String getTicketUploadDir() {
        return this.ticketUploadDir;
    }

    public void setTicketUploadDir(String ticketUploadDir) {
        this.ticketUploadDir = ticketUploadDir;
    }

    public String getManualsUploadDir() {
        return this.manualsUploadDir;
    }

    public void setManualsUploadDir(String manualsUploadDir) {
        this.manualsUploadDir = manualsUploadDir;
    }

}
