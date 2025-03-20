package com.example.ticket_platform.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class ApiUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Il campo username è obbligatorio")
    private String username;

    @NotBlank(message = "Il campo password è obbligatorio")
    private String password;

    @NotBlank(message = "L'authKey è obbligatoria")
    private String authKey;

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

    public String getAuthKey() {
        return this.authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

}
