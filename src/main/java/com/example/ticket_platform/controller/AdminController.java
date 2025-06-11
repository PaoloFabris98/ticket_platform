package com.example.ticket_platform.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.dto.TempUser;
import com.example.ticket_platform.repository.UserRepository;
import com.example.ticket_platform.repository.UserStatusRepository;
import com.example.ticket_platform.security.CustomJdbcUserDetailsManager;
import com.example.ticket_platform.service.AuthoritiesService;
import com.example.ticket_platform.service.TicketService;
import com.example.ticket_platform.service.UserService;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomJdbcUserDetailsManager customJdbcUserDetailsManager;
    @Autowired
    private UserStatusRepository userStatusRepository;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private AuthoritiesService authoritiesService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UtilityFunctions utilityFunctions;

    @ModelAttribute("currentUser")
    public String getCurrentUser(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return utilityFunctions.currentUser(principal).getUsername();
        }
        return "redirect:/login";
    }

    @ModelAttribute("currentUserObj")
    public TempUser getCurrentUserObj(Principal principal) {
        User user = utilityFunctions.currentUser(principal);

        if (user == null) {
            return null;
        }

        TempUser tempUser = new TempUser();
        tempUser.setId(user.getId());
        tempUser.setUsername(user.getUsername());
        tempUser.setRole(user.getRole());
        tempUser.setUserStatus(user.getUserStatus());
        return tempUser;
    }

    @GetMapping("/admin_panel")
    public String adminPanel(Model model, Principal principal) {
        utilityFunctions.checkingAdminPermission(principal);

        return "adminDashboard/index";
    }
}
