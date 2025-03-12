package com.example.ticket_platform.config;

import com.example.ticket_platform.model.Status;
import com.example.ticket_platform.model.StatusType;
import com.example.ticket_platform.repository.StatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class StatusInitializer {

    @Bean
    public CommandLineRunner initStatuses(StatusRepository statusRepository) {
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
        };
    }
}
