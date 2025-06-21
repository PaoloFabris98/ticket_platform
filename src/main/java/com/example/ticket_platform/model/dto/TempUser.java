package com.example.ticket_platform.model.dto;

import com.example.ticket_platform.model.AuthoritiesType;
import com.example.ticket_platform.model.UserStatus;

public class TempUser {
    private Integer id;
    private String username;
    private AuthoritiesType role;
    private UserStatus userStatus;
    private String apiAuthKey;
    private String getAllTicketAuthKey;

    public TempUser() {
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AuthoritiesType getRole() {
        return this.role;
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

    public UserStatus getUserStatus() {
        return this.userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public String getApiAuthKey() {
        return this.apiAuthKey;
    }

    public void setApiAuthKey(String apiAuthKey) {
        this.apiAuthKey = apiAuthKey;
    }

    public String getGetAllTicketAuthKey() {
        return this.getAllTicketAuthKey;
    }

    public void setGetAllTicketAuthKey(String getAllTicketAuthKey) {
        this.getAllTicketAuthKey = getAllTicketAuthKey;
    }

}
