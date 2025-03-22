package com.example.ticket_platform.config;

import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.*;
import com.example.ticket_platform.repository.*;
import com.example.ticket_platform.security.CustomJdbcUserDetailsManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    private final UserRepository userRepository;

    private final CustomJdbcUserDetailsManager customJdbcUserDetailsManager;

    DataInitializer(CustomJdbcUserDetailsManager customJdbcUserDetailsManager, UserRepository userRepository) {
        this.customJdbcUserDetailsManager = customJdbcUserDetailsManager;
        this.userRepository = userRepository;
    }

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, TicketRepository ticketRepository,
            StatusRepository statusRepository, AuthoritiesRepository authoritiesRepository,
            UserStatusRepository userStatusRepository, CategoriaRepository categoriaRepository,
            ApiUserRepository apiUserRepository, UtilityFunctions utilityFunctions) {
        return test -> {

            if (statusRepository.count() == 0) {
                Status aperto = new Status();
                aperto.setStatus(StatusType.APERTO);

                Status inCorso = new Status();
                inCorso.setStatus(StatusType.IN_CORSO);

                Status chiuso = new Status();
                chiuso.setStatus(StatusType.CHIUSO);

                statusRepository.saveAll(Arrays.asList(aperto, inCorso, chiuso));
            }

            Status statusAperto = statusRepository.findByStatus(StatusType.APERTO);

            if (userStatusRepository.count() == 0) {
                UserStatus attivo = new UserStatus();
                attivo.setUserStatusType(UserStatusType.DISPONIBILE);

                UserStatus nonAttivo = new UserStatus();
                nonAttivo.setUserStatusType(UserStatusType.NON_DISPONIBILE);

                userStatusRepository.saveAll(Arrays.asList(attivo, nonAttivo));
            }

            UserStatus statusAttivo = userStatusRepository.findByUserStatusType(UserStatusType.DISPONIBILE);
            UserStatus statusNonAttivo = userStatusRepository.findByUserStatusType(UserStatusType.NON_DISPONIBILE);

            if (!(userRepository.findByUsername("Operatore").isEmpty())) {
                User user = new User();
                user.setUsername("Operatore");
                user.setPassword("123");
                user.setEmail("test@example.com");
                user.setEnable(true);
                user.setRole(AuthoritiesType.USER);
                user.setUserStatus(statusAttivo);

                Authorities authorities = new Authorities();
                authorities.setUsername(user.getUsername());
                authorities.setAuthority("USER");
                authoritiesRepository.save(authorities);
                customJdbcUserDetailsManager.create(user);
            }

            if (!userRepository.findByUsername("Operatore").isPresent()) {
                User user = new User();
                user.setUsername("Operatore");
                user.setPassword("123");
                user.setEmail("test@example.com");
                user.setEnable(true);
                user.setRole(AuthoritiesType.USER);
                user.setUserStatus(statusAttivo);

                customJdbcUserDetailsManager.create(user);
            }

            if (!userRepository.findByUsername("Admin").isPresent()) {
                User user = new User();
                user.setUsername("Admin");
                user.setPassword("123");
                user.setEmail("test@example.com");
                user.setEnable(true);
                user.setRole(AuthoritiesType.ADMIN);
                user.setUserStatus(statusAttivo);

                customJdbcUserDetailsManager.create(user);
            }

            if (!userRepository.findByUsername("Guest").isPresent()) {
                User user = new User();
                user.setUsername("Guest");
                user.setPassword("123");
                user.setEmail("test@example.com");
                user.setEnable(true);
                user.setRole(AuthoritiesType.USER);
                user.setUserStatus(statusAttivo);

                customJdbcUserDetailsManager.create(user);
            }

            if (categoriaRepository.count() == 0) {
                Categoria assistenza = new Categoria(CategoriaTicketType.ASSISTENZA);
                Categoria manutenzione = new Categoria(CategoriaTicketType.MANUTENZIONE);
                Categoria amministrazione = new Categoria(CategoriaTicketType.AMMINISTRAZIONE);
                Categoria tecnica = new Categoria(CategoriaTicketType.TECNICA);

                categoriaRepository.saveAll(Arrays.asList(assistenza, manutenzione, amministrazione, tecnica));
            }

            Categoria categoriaAssistenza = categoriaRepository.findByNome(CategoriaTicketType.ASSISTENZA);
            Categoria categoriaManutenzione = categoriaRepository.findByNome(CategoriaTicketType.MANUTENZIONE);
            User operatore = userRepository.findByUsername("Operatore").get();

            if (ticketRepository.countByOperatore(operatore) < 5) {
                for (int i = 1; i <= 5; i++) {
                    Ticket ticket = new Ticket();
                    ticket.setOperatore(operatore);
                    ticket.setDataCreazione(LocalDate.now());
                    ticket.setDescrizione("Ticket di test numero " + i);
                    ticket.setStatus(statusAperto);
                    ticket.setCategoria(i % 2 == 0 ? categoriaManutenzione : categoriaAssistenza);
                    ticketRepository.save(ticket);
                }
            }
            User admin = userRepository.findByUsername("Admin").get();
            if (ticketRepository.countByOperatore(admin) < 5) {
                for (int i = 1; i <= 5; i++) {
                    Ticket ticket = new Ticket();
                    ticket.setOperatore(admin);
                    ticket.setDataCreazione(LocalDate.now());
                    ticket.setDescrizione("Ticket di test numero " + i);
                    ticket.setStatus(statusAperto);
                    ticket.setCategoria(i % 2 == 0 ? categoriaManutenzione : categoriaAssistenza);
                    ticketRepository.save(ticket);
                }
            }
            if (apiUserRepository.count() == 0) {
                ApiUser apiUser = new ApiUser();
                apiUser.setUsername("testUser");
                apiUser.setPassword("testPassword");
                apiUser.setAuthKey(utilityFunctions.authKeyGenerator(20));

                apiUserRepository.save(apiUser);
            }

        };
    }
}
