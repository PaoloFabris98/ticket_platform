package com.example.ticket_platform.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticket_platform.model.StatusType;
import com.example.ticket_platform.repository.StatusRepository;

@Service
public class StatusService {

    @Autowired
    StatusRepository statusRepository;

    public List<StatusType> getAllStatusTypes() {
        List<StatusType> statusTypesList = Arrays.asList(StatusType.values());
        return statusTypesList;
    }

}
