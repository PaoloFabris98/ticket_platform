package com.example.ticket_platform.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.ticket_platform.model.Authorities;
import com.example.ticket_platform.model.Status;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.repository.AuthoritiesRepository;

public class DatabaseUserDetails implements UserDetails {

    private final Integer id;
    private final String username;
    private final String password;
    private final String email;
    private final Boolean enable;
    // private final String status;
    private final Set<GrantedAuthority> authorities;

    public DatabaseUserDetails(User user, AuthoritiesRepository authoritiesRepository) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.enable = user.getEnable();
        // this.status = user.getStatus();
        this.authorities = new HashSet<>();

        List<Authorities> userAuthorities = authoritiesRepository.findByUsername(username);
        for (Authorities authority : userAuthorities) {
            this.authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public Integer getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public Boolean isEnable() {
        return this.enable;
    }

    /*
     * public String getStatus() {
     * return this.status;
     * }
     */

    @Override
    public String toString() {
        return this.getUsername();
    }
}
