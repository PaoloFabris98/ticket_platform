package com.example.ticket_platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.ApiUser;
import com.example.ticket_platform.repository.ApiUserRepository;

@Service
public class ApiUserService {

    @Autowired
    private UtilityFunctions utilityFunctions;
    @Autowired
    private ApiUserRepository apiUserRepository;

    public void saveApiUser(ApiUser apiUser) {
        apiUserRepository.save(apiUser);
    }
}
