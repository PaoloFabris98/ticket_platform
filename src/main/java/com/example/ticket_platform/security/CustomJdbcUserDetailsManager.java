package com.example.ticket_platform.security;

import java.security.Principal;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.transaction.annotation.Transactional;

import com.example.ticket_platform.model.Authorities;
import com.example.ticket_platform.model.Magazzino;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.UserStatus;
import com.example.ticket_platform.model.UserStatusType;
import com.example.ticket_platform.repository.UserRepository;
import com.example.ticket_platform.repository.UserStatusRepository;
import com.example.ticket_platform.service.AuthoritiesService;
import com.example.ticket_platform.repository.AuthoritiesRepository;
import com.example.ticket_platform.repository.MagazzinoRepository;

public class CustomJdbcUserDetailsManager extends JdbcUserDetailsManager {

    private UserRepository userRepository;
    private AuthoritiesRepository authoritiesRepository;

    @Autowired
    private AuthoritiesService authoritiesService;
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private MagazzinoRepository magazzinoRepository;

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
            throw new IllegalArgumentException("Lo username " + user.getUsername() + " è già in uso.");
        }
        createUserAuthoriti(user);
        Magazzino magazzino = new Magazzino();
        magazzino.setName("VanKid" + user.getUsername());
        magazzino.setProprietario(user);
        user.setVanKit(magazzino);

        if (user.getEnable() == false) {
            user.setUserStatus(userStatusRepository.findByUserStatusType(UserStatusType.NON_DISPONIBILE));
        }

        userRepository.save(user);
        DatabaseUserDetails databaseUserDetails = new DatabaseUserDetails(user, authoritiesRepository);
        updateAuthoritiesV1(databaseUserDetails);

    }

    public void updateUserApiKey(User user, Principal principal) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        existingUser.setAllTicketAuthKeyLastUpdated(user.getAllTicketAuthKeyLastUpdated());
        existingUser.setAllTicketAuthKey(user.getAllTicketAuthKey());
        existingUser.setApiAuthKey(user.getApiAuthKey());
        existingUser.setApiAuthKeyLastUpdated(user.getApiAuthKeyLastUpdated());
        existingUser.setTickets(user.getTickets());
        try {
            userRepository.save(existingUser);
        } catch (Exception e) {
            throw new IllegalArgumentException("Operazione fallita");
        }
    }

    @Transactional
    public void updateUser(User user, Principal principal) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        String oldUsername = existingUser.getUsername();
        UserStatus userStatus = userStatusRepository
                .findByUserStatusType(existingUser.getUserStatus().getUserStatusType());

        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setEmail(user.getEmail());
        existingUser.setEnable(user.getEnable());
        existingUser.setRole(user.getRole());
        existingUser.setUserStatus(userStatus);
        existingUser.setAllTicketAuthKeyLastUpdated(user.getAllTicketAuthKeyLastUpdated());
        existingUser.setAllTicketAuthKey(user.getAllTicketAuthKey());
        existingUser.setApiAuthKey(user.getApiAuthKey());
        existingUser.setApiAuthKeyLastUpdated(user.getApiAuthKeyLastUpdated());
        existingUser.setTickets(user.getTickets());
        existingUser.setVanKit(user.getVanKit());

        System.out.println("Aggiornando l'utente: " + existingUser.getUsername());
        try {
            userRepository.save(existingUser);
        } catch (Exception e) {
            throw new IllegalArgumentException("Operazione fallita");
        }
        System.out.println("Utente salvato: " + existingUser.getUsername());

        DatabaseUserDetails databaseUserDetails = new DatabaseUserDetails(existingUser, authoritiesRepository);
        System.out.println("creazione databaseUserDetails");

        updateAuthoritiesV3(databaseUserDetails, oldUsername, user);

        System.out.println("aggiornamento authorities");

        expiringSessionAfterUserUpdate(oldUsername, databaseUserDetails);
    }

    @Transactional
    private void updateAuthoritiesV3(DatabaseUserDetails databaseUserDetails, String oldUsername, User user) {
        Authorities oldAuthorities = authoritiesService.findByUsername(oldUsername);
        oldAuthorities.setUsername(databaseUserDetails.getUsername());
        oldAuthorities.setAuthority(user.getRole().getName());
        authoritiesRepository.save(oldAuthorities);
        System.out.println("Nuove authorities");
    }

    @Transactional
    private void updateAuthoritiesV1(DatabaseUserDetails databaseUserDetails) {
        authoritiesRepository.deleteByUsername(databaseUserDetails.getUsername());

        databaseUserDetails.getAuthorities().forEach(authority -> {
            authoritiesRepository.save(new Authorities(databaseUserDetails.getUsername(), authority.getAuthority()));
        });
    }

    @Transactional
    private void createUserAuthoriti(User user) {
        if (authoritiesRepository.findByUsername(user.getUsername()).isEmpty()) {
            Authorities authorities = new Authorities();
            authorities.setUsername(user.getUsername());
            authorities.setAuthority(user.getRole().getName());
            authoritiesRepository.save(authorities);
        }

    }

    private void expiringSessionAfterUserUpdate(String oldUsername, DatabaseUserDetails updatedUserDetails) {
        sessionRegistry.getAllPrincipals().forEach(principal -> {
            String currentUsername = null;

            if (principal instanceof UserDetails) {
                currentUsername = ((UserDetails) principal).getUsername();
            } else {
                currentUsername = principal.toString();
            }

            if (currentUsername.equals(oldUsername)) {
                sessionRegistry.getAllSessions(principal, false).forEach(session -> {
                    session.expireNow();
                });
            }
        });
    }

    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

}
