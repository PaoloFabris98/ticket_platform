package com.example.ticket_platform.security;

import java.security.Principal;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.transaction.annotation.Transactional;

import com.example.ticket_platform.model.Authorities;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.UserStatus;
import com.example.ticket_platform.repository.UserRepository;
import com.example.ticket_platform.repository.UserStatusRepository;

import com.example.ticket_platform.repository.AuthoritiesRepository;

public class CustomJdbcUserDetailsManager extends JdbcUserDetailsManager {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthoritiesRepository authoritiesRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;

    public CustomJdbcUserDetailsManager(DataSource dataSource, UserRepository userRepository,
            AuthoritiesRepository authoritiesRepository) {
        super(dataSource);
        this.userRepository = userRepository;
        this.authoritiesRepository = authoritiesRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));

        return new DatabaseUserDetails(user, authoritiesRepository);
    }

    @Transactional
    public void create(User user) {
        if (userExists(user.getUsername())) {
            throw new IllegalArgumentException("L'utente esiste già!");
        }
        createUserAuthoriti(user);
        userRepository.save(user);
        DatabaseUserDetails databaseUserDetails = new DatabaseUserDetails(user, authoritiesRepository);
        updateAuthorities(databaseUserDetails);

    }

    @Transactional
    public void updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).get();

        UserStatus userStatus = userStatusRepository
                .findByUserStatusType(existingUser.getUserStatus().getUserStatusType());

        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setEmail(user.getEmail());
        existingUser.setEnable(user.getEnable());
        existingUser.setUserStatus(userStatus);
        System.out.println("Aggiornando l'utente: " + existingUser);
        userRepository.save(existingUser);
        System.out.println("Utente salvato: " + existingUser);

        DatabaseUserDetails databaseUserDetails = new DatabaseUserDetails(existingUser, authoritiesRepository);

        updateAuthorities(databaseUserDetails);
    }

    public void updateUsernameReferences(String oldUsername, User newUser) {
        User user = userRepository.findByUsername(oldUsername).get();
        if (user != null) {
            user.setUsername(newUser.getUsername());
            Authorities authorities = authoritiesRepository.findByUsername(oldUsername).get(0);
            authorities.setUsername(newUser.getUsername());
            authoritiesRepository.save(authorities);
            userRepository.save(user);

        }
    }

    @Transactional
    private void updateAuthorities(UserDetails userDetails) {
        authoritiesRepository.deleteByUsername(userDetails.getUsername());

        userDetails.getAuthorities().forEach(authority -> {
            authoritiesRepository.save(new Authorities(userDetails.getUsername(), authority.getAuthority()));
        });
    }

    @Transactional
    private void createUserAuthoriti(User user) {
        Authorities authorities = new Authorities();
        authorities.setUsername(user.getUsername());
        authorities.setAuthority("USER");

        authoritiesRepository.save(authorities);
    }

}
