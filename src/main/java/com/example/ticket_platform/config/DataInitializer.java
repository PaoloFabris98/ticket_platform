package com.example.ticket_platform.config;

import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.*;
import com.example.ticket_platform.repository.*;
import com.example.ticket_platform.security.CustomJdbcUserDetailsManager;
import com.example.ticket_platform.service.ArticoloService;
import com.example.ticket_platform.service.CodiceService;
import com.example.ticket_platform.service.MagazzinoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    private final CustomJdbcUserDetailsManager customJdbcUserDetailsManager;

    DataInitializer(CustomJdbcUserDetailsManager customJdbcUserDetailsManager) {
        this.customJdbcUserDetailsManager = customJdbcUserDetailsManager;
    }

    @Autowired
    private OptionsRepository optionsRepository;

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, TicketRepository ticketRepository,
            StatusRepository statusRepository, AuthoritiesRepository authoritiesRepository,
            UserStatusRepository userStatusRepository, CategoriaRepository categoriaRepository,
            ApiUserRepository apiUserRepository, UtilityFunctions utilityFunctions,
            ClienteRepository clienteRepository, MagazzinoRepository magazzinoRepository,
            ArticoloRepository articoloRepository, CodiceRepository codiceRepository) {
        return test -> {

            Options options = new Options();
            options.setTicketUploadDir("uploads/ticket/");
            options.setManualsUploadDir("uploads/manuali/");
            optionsRepository.save(options);

            if (statusRepository.count() == 0) {
                Status aperto = new Status();
                aperto.setStatus("APERTO");

                Status inCorso = new Status();
                inCorso.setStatus("IN_CORSO");

                Status chiuso = new Status();
                chiuso.setStatus("CHIUSO");

                statusRepository.saveAll(Arrays.asList(aperto, inCorso, chiuso));
            }

            if (!clienteRepository.findByNome("Boolean").isPresent()) {
                Cliente cliente = new Cliente();
                cliente.setNome("Boolean");
                cliente.setCellulare("3891372590");
                cliente.setEmail("f98paolo@gmail.com");
                clienteRepository.save(cliente);
            }
            if (!clienteRepository.findByNome("mediaword").isPresent()) {
                Cliente cliente = new Cliente();
                cliente.setNome("mediaword");
                cliente.setCellulare("3891372591");
                cliente.setEmail("f98paoloTest@gmail.com");
                clienteRepository.save(cliente);
            }

            Status statusAperto = statusRepository.findByStatus("APERTO");

            if (userStatusRepository.count() == 0) {
                UserStatus attivo = new UserStatus();
                attivo.setUserStatusType(UserStatusType.DISPONIBILE);

                UserStatus nonAttivo = new UserStatus();
                nonAttivo.setUserStatusType(UserStatusType.NON_DISPONIBILE);

                userStatusRepository.saveAll(Arrays.asList(attivo, nonAttivo));
            }

            UserStatus statusAttivo = userStatusRepository.findByUserStatusType(UserStatusType.DISPONIBILE);
            UserStatus statusNonAttivo = userStatusRepository.findByUserStatusType(UserStatusType.NON_DISPONIBILE);

            if (magazzinoRepository.count() == 0) {
                Magazzino magazzino = new Magazzino();
                magazzino.setName("Sede");
                magazzinoRepository.save(magazzino);
            }

            if (!userRepository.findByUsername("Operatore").isPresent()) {
                User user = new User();
                user.setUsername("Operatore");
                user.setPassword("123");
                user.setEmail("test@example.com");
                user.setEnable(true);
                user.setRole(AuthoritiesType.USER);
                user.setUserStatus(statusAttivo);
                user.setApiAuthKey(utilityFunctions.authKeyGenerator(30));
                user.setAllTicketAuthKey(utilityFunctions.authKeyGenerator(30));
                user.setAllTicketAuthKeyLastUpdated(LocalDateTime.now());
                user.setApiAuthKeyLastUpdated(LocalDateTime.now());
                String tempStr = "VanKitOperatore";
                Magazzino tempMagazzino = new Magazzino();
                tempMagazzino.setName(tempStr);
                tempMagazzino.setProprietario(user);
                user.setVanKit(tempMagazzino);
                Authorities authorities = new Authorities();
                authorities.setUsername(user.getUsername());
                authorities.setAuthority("USER");
                authoritiesRepository.save(authorities);
                customJdbcUserDetailsManager.create(user);
            }

            if (!userRepository.findByUsername("Non Assegnati").isPresent()) {
                User user = new User();
                user.setUsername("Non Assegnati");
                user.setPassword("123");
                user.setEmail("NonAssegnati@example.com");
                user.setEnable(false);
                user.setRole(AuthoritiesType.USER);
                user.setUserStatus(statusNonAttivo);
                Authorities authorities = new Authorities();
                authorities.setUsername(user.getUsername());
                authorities.setAuthority("USER");
                authoritiesRepository.save(authorities);
                customJdbcUserDetailsManager.create(user);
            }

            if (!userRepository.findByUsername("Admin").isPresent()) {
                User user = new User();
                user.setUsername("Admin");
                user.setPassword("123");
                user.setEmail("tesAdmin@example.com");
                user.setEnable(true);
                user.setApiAuthKey(utilityFunctions.authKeyGenerator(30));
                user.setAllTicketAuthKey(utilityFunctions.authKeyGenerator(30));
                user.setRole(AuthoritiesType.ADMIN);
                user.setUserStatus(statusAttivo);
                user.setAllTicketAuthKeyLastUpdated(LocalDateTime.now());
                user.setApiAuthKeyLastUpdated(LocalDateTime.now());

                String tempStr = "VanKitAdmin";
                Magazzino tempMagazzino = new Magazzino();
                tempMagazzino.setName(tempStr);
                tempMagazzino.setProprietario(user);
                user.setVanKit(tempMagazzino);

                customJdbcUserDetailsManager.create(user);
            }

            if (!userRepository.findByUsername("Guest").isPresent()) {
                User user = new User();
                user.setUsername("Guest");
                user.setPassword("123");
                user.setEmail("te2st@example.com");
                user.setEnable(true);
                user.setRole(AuthoritiesType.USER);
                user.setUserStatus(statusNonAttivo);

                customJdbcUserDetailsManager.create(user);
            }

            if (categoriaRepository.count() == 0) {
                Categoria assistenza = new Categoria("ASSISTENZA");
                Categoria manutenzione = new Categoria("MANUTENZIONE");
                Categoria amministrazione = new Categoria("AMMINISTRAZIONE");
                Categoria tecnica = new Categoria("TECNICA");

                categoriaRepository.saveAll(Arrays.asList(assistenza, manutenzione, amministrazione, tecnica));
            }

            Categoria categoriaAssistenza = categoriaRepository.findByNome("ASSISTENZA");
            Categoria categoriaManutenzione = categoriaRepository.findByNome("MANUTENZIONE");
            Cliente cliente = clienteRepository.findByNome("Boolean").get();
            Cliente mediaCliente = clienteRepository.findByNome("mediaword").get();
            User operatore = userRepository.findByUsername("Operatore").get();

            if (ticketRepository.countByOperatore(operatore) < 5) {
                for (int i = 1; i <= 5; i++) {
                    Ticket ticket = new Ticket();
                    ticket.setOperatore(operatore);
                    ticket.setDataCreazione(LocalDate.now());
                    ticket.setDescrizione("Ticket di test numero " + i);
                    ticket.setStatus(statusAperto);
                    ticket.setCliente(cliente);
                    ticket.setCategoria(i % 2 == 0 ? categoriaManutenzione : categoriaAssistenza);
                    ticketRepository.save(ticket);
                }
            }
            if (ticketRepository.countByOperatore(operatore) < 10) {
                for (int i = 1; i <= 5; i++) {
                    Ticket ticket = new Ticket();
                    ticket.setOperatore(operatore);
                    ticket.setDataCreazione(LocalDate.now().minusDays(1));
                    ticket.setDataChiusura(LocalDate.now().minusDays(1));
                    ticket.setDescrizione("Ticket di test numero " + i);
                    ticket.setStatus(statusRepository.findByStatus("CHIUSO"));
                    ticket.setCliente(cliente);
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
                    ticket.setCliente(mediaCliente);
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
