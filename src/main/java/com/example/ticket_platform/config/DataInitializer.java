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
            StatusRepository statusRepository, AuthoritiesRepository authoritiesRepository) {
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

            User testOperatore = userRepository.findByUsername("test")
                    .orElseGet(() -> {
                        User user = new User();
                        user.setUsername("test");
                        user.setPassword("123");
                        user.setEmail("test@example.com");
                        user.setEnable(true);
                        Authorities authorities = new Authorities();
                        authorities.setUsername(user.getUsername());
                        authorities.setAuthority("USER");
                        authoritiesRepository.save(authorities);
                        return userRepository.save(user);
                    });

            if (ticketRepository.countByOperatore(testOperatore) < 5) {
                for (int i = 1; i <= 5; i++) {
                    Ticket ticket = new Ticket();
                    ticket.setOperatore(testOperatore);
                    ticket.setDataCreazione(LocalDate.now());
                    ticket.setDescrizione("Ticket di test numero " + i);
                    ticket.setStatus(statusAperto);
                    ticketRepository.save(ticket);
                }
            }
        };
    }
}
