package com.example.ticket_platform.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticket_platform.model.Status;
import com.example.ticket_platform.model.StatusType;
import com.example.ticket_platform.repository.AuthoritiesRepository;
import com.example.ticket_platform.repository.StatusRepository;

@Service
public class StatusService {

    private final AuthoritiesRepository authoritiesRepository;
    @Autowired
    StatusRepository statusRepository;

    StatusService(AuthoritiesRepository authoritiesRepository) {
        this.authoritiesRepository = authoritiesRepository;
    }

    public List<StatusType> getAllStatusTypes() {
        List<StatusType> statusTypesList = Arrays.asList(StatusType.values());
        return statusTypesList;
    }

}
