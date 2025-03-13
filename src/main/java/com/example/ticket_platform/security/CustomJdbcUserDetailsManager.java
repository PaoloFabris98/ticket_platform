package com.example.ticket_platform.security;

import javax.sql.DataSource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.repository.UserRepository;
import com.example.ticket_platform.repository.AuthoritiesRepository;

public class CustomJdbcUserDetailsManager extends JdbcUserDetailsManager {

    private final UserRepository userRepository;
    private final AuthoritiesRepository authoritiesRepository;

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

    @Override
    public void createUser(UserDetails user) {
        if (userExists(user.getUsername())) {
            throw new IllegalArgumentException("L'utente esiste già!");
        }
        super.createUser(user);

    }
}
