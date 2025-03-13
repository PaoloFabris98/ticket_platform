package com.example.ticket_platform.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Lo Username non può essere vuoto.")
    private String username;

    @NotBlank(message = "La Password non può essere vuota.")
    private String password;

    @NotBlank(message = "La Mail non può essere vuota.")
    private String email;

    @Column(name = "enabled")
    private Boolean enable = true;

    @Column(name = "tickets")
    @OneToMany(mappedBy = "operatore")
    private List<Ticket> tickets;

    // @NotBlank(message = "Lo status non può essere vuoto")
    // private String status;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Ticket> getTickets() {
        return this.tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    /*
     * public String getStatus() {
     * return this.status;
     * }
     * 
     * public void setStatus(String status) {
     * this.status = status;
     * }
     */

    public Boolean isEnable() {
        return this.enable;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

}
