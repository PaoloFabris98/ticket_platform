package com.example.ticket_platform.config;

import com.example.ticket_platform.model.*;
import com.example.ticket_platform.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, TicketRepository ticketRepository,
            StatusRepository statusRepository, AuthoritiesRepository authoritiesRepository,
            UserStatusRepository userStatusRepository, CategoriaRepository categoriaRepository) {
        return args -> {

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

            User testOperatore = userRepository.findByUsername("Operatore")
                    .orElseGet(() -> {
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
                        return userRepository.save(user);
                    });
            User testAdmin = userRepository.findByUsername("Admin")
                    .orElseGet(() -> {
                        User user = new User();
                        user.setUsername("Admin");
                        user.setPassword("123");
                        user.setEmail("admin@example.com");
                        user.setEnable(true);
                        user.setRole(AuthoritiesType.ADMIN);
                        user.setUserStatus(statusAttivo);

                        Authorities authorities = new Authorities();
                        authorities.setUsername(user.getUsername());
                        authorities.setAuthority("ADMIN");
                        authoritiesRepository.save(authorities);
                        return userRepository.save(user);
                    });

            User testGuest = userRepository.findByUsername("Guest")
                    .orElseGet(() -> {
                        User user = new User();
                        user.setUsername("Guest");
                        user.setPassword("guest123");
                        user.setEmail("guest@example.com");
                        user.setEnable(false);
                        user.setRole(AuthoritiesType.USER);
                        user.setUserStatus(statusNonAttivo);

                        Authorities authorities = new Authorities();
                        authorities.setUsername(user.getUsername());
                        authorities.setAuthority("USER");
                        authoritiesRepository.save(authorities);
                        return userRepository.save(user);
                    });

            if (categoriaRepository.count() == 0) {
                Categoria assistenza = new Categoria(CategoriaTicketType.ASSISTENZA);
                Categoria manutenzione = new Categoria(CategoriaTicketType.MANUTENZIONE);
                Categoria amministrazione = new Categoria(CategoriaTicketType.AMMINISTRAZIONE);
                Categoria tecnica = new Categoria(CategoriaTicketType.TECNICA);

                categoriaRepository.saveAll(Arrays.asList(assistenza, manutenzione, amministrazione, tecnica));
            }

            Categoria categoriaAssistenza = categoriaRepository.findByNome(CategoriaTicketType.ASSISTENZA);
            Categoria categoriaManutenzione = categoriaRepository.findByNome(CategoriaTicketType.MANUTENZIONE);

            if (ticketRepository.countByOperatore(testOperatore) < 5) {
                for (int i = 1; i <= 5; i++) {
                    Ticket ticket = new Ticket();
                    ticket.setOperatore(testOperatore);
                    ticket.setDataCreazione(LocalDate.now());
                    ticket.setDescrizione("Ticket di test numero " + i);
                    ticket.setStatus(statusAperto);
                    ticket.setCategoria(i % 2 == 0 ? categoriaManutenzione : categoriaAssistenza);
                    ticketRepository.save(ticket);
                }
            }

            if (ticketRepository.countByOperatore(testAdmin) < 5) {
                for (int i = 1; i <= 5; i++) {
                    Ticket ticket = new Ticket();
                    ticket.setOperatore(testAdmin);
                    ticket.setDataCreazione(LocalDate.now());
                    ticket.setDescrizione("Ticket di test numero " + i);
                    ticket.setStatus(statusAperto);
                    ticket.setCategoria(i % 2 == 0 ? categoriaManutenzione : categoriaAssistenza);
                    ticketRepository.save(ticket);
                }
            }

        };
    }
}
