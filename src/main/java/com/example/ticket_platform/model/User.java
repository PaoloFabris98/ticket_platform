package com.example.ticket_platform.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    @NotBlank(message = "Lo Username non può essere vuoto.")
    private String username;

    @JsonIgnore
    private String password;

    @NotBlank(message = "La Mail non può essere vuota.")
    @Column(unique = true)
    @JsonIgnore
    private String email;

    private String apiAuthKey;
    private LocalDateTime apiAuthKeyLastUpdated;
    private String allTicketAuthKey;
    private LocalDateTime allTicketAuthKeyLastUpdated;

    @Column(name = "enabled")
    @JsonIgnore
    private Boolean enable = true;

    @Column(name = "tickets")
    @OneToMany(mappedBy = "operatore", orphanRemoval = true)
    @JsonBackReference
    private List<Ticket> tickets;

    @ManyToOne
    @JoinColumn(name = "user_status_id")
    @JsonIgnore
    private UserStatus userStatus;
    @JsonIgnore
    private AuthoritiesType role;

    @OneToOne(mappedBy = "proprietario")
    private Magazzino vanKit;

    public AuthoritiesType getRole() {
        return role;
    }

    public void setRole(AuthoritiesType role) {
        this.role = role;
    }

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

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Boolean isEnable() {
        return this.enable;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getApiAuthKey() {
        return this.apiAuthKey;
    }

    public void setApiAuthKey(String apiAuthKey) {
        this.apiAuthKey = apiAuthKey;
    }

    public String getAllTicketAuthKey() {
        return this.allTicketAuthKey;
    }

    public void setAllTicketAuthKey(String allTicketAuthKey) {
        this.allTicketAuthKey = allTicketAuthKey;
    }

    public LocalDateTime getApiAuthKeyLastUpdated() {
        return this.apiAuthKeyLastUpdated;
    }

    public void setApiAuthKeyLastUpdated(LocalDateTime apiAuthKeyLastUpdated) {
        this.apiAuthKeyLastUpdated = apiAuthKeyLastUpdated;
    }

    public LocalDateTime getAllTicketAuthKeyLastUpdated() {
        return this.allTicketAuthKeyLastUpdated;
    }

    public void setAllTicketAuthKeyLastUpdated(LocalDateTime allTicketAuthKeyLastUpdated) {
        this.allTicketAuthKeyLastUpdated = allTicketAuthKeyLastUpdated;
    }

    public Magazzino getVanKit() {
        return this.vanKit;
    }

    public void setVanKit(Magazzino vanKit) {
        this.vanKit = vanKit;
    }

}
