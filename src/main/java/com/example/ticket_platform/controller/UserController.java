package com.example.ticket_platform.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.UserStatus;
import com.example.ticket_platform.model.UserStatusType;
import com.example.ticket_platform.model.dto.TempUser;
import com.example.ticket_platform.repository.UserRepository;
import com.example.ticket_platform.repository.UserStatusRepository;
import com.example.ticket_platform.security.CustomJdbcUserDetailsManager;
import com.example.ticket_platform.service.AuthoritiesService;
import com.example.ticket_platform.service.TicketService;
import com.example.ticket_platform.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

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
        tempUser.setApiAuthKey(user.getApiAuthKey());
        tempUser.setAllTicketAuthKey(user.getAllTicketAuthKey());
        return tempUser;
    }

    @GetMapping("/operatori")
    public String seeOperators(Model model, Principal principal) {
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
        model.addAttribute("currentPage", "/operatori");

        return "user/index";
    }

    @GetMapping("/dashboard/{id}")
    public String dashboard(@PathVariable Integer id, Model model, Principal principal) {
        try {
            User user = userService.findUserById(id);
            if (utilityFunctions.isAdmin(utilityFunctions.currentUser(principal))) {
                model.addAttribute("user", user);
            } else {
                if (utilityFunctions.currentUser(principal).getId() == id) {
                    model.addAttribute(user);
                } else {
                    return "redirect:/permissions_missing";
                }
            }

            return "user/dashboard";
        } catch (Exception e) {
            return "redirect:/user_Index_Out_Of_Bound";
        }

    }

    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable Integer id, Model model, Principal principal) {
        User user = userService.findUserById(id);

        if (utilityFunctions.isAdmin(utilityFunctions.currentUser(principal))) {
            model.addAttribute("user", user);
            model.addAttribute("roles", authoritiesService.getAllAuthoritiesTypes());
            model.addAttribute("currentRole", user.getRole());

            return "user/editUserForAdmin";
        } else {
            if (utilityFunctions.currentUser(principal).getId() == id) {
                model.addAttribute(user);
                return "user/edit";
            } else {
                return "redirect:/permissions_missing";
            }
        }
    }

    @PostMapping("/editUser/{id}")
    public String editUser(@PathVariable Integer id, @Valid @ModelAttribute("user") User formUser,
            BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes, Principal principal) {
        User user = userService.findUserById(id);
        if (utilityFunctions.isAdmin(utilityFunctions.currentUser(principal))) {
            if (userRepository.existsByUsername(formUser.getUsername())
                    && !formUser.getUsername().equals(user.getUsername())) {
                bindingResult.rejectValue("username", "duplicate", "Username già esistente.");
            }

            if (userRepository.existsByEmail(formUser.getEmail()) && !formUser.getEmail().equals(user.getEmail())) {
                bindingResult.rejectValue("email", "duplicate", "Email già esistente.");
            }
            if (bindingResult.hasErrors()) {

                model.addAttribute("roles", authoritiesService.getAllAuthoritiesTypes());
                model.addAttribute("currentRole", user.getRole());
                return "user/editUserForAdmin";
            }
            if (formUser.getPassword().equals("")) {
                formUser.setPassword(user.getPassword());
            }
            customJdbcUserDetailsManager.updateUser(formUser, principal);
            redirectAttributes.addFlashAttribute("message", "Utente aggiornato correttamente!");
            redirectAttributes.addFlashAttribute("messageClass", "alert-success");

            return "redirect:/operatori";
        } else {
            if (userRepository.existsByUsername(formUser.getUsername())
                    && !formUser.getUsername().equals(user.getUsername())) {
                bindingResult.rejectValue("username", "duplicate", "Username già esistente.");
            }

            if (userRepository.existsByEmail(formUser.getEmail()) && !formUser.getEmail().equals(user.getEmail())) {
                bindingResult.rejectValue("email", "duplicate", "Email già esistente.");
            }
            if (bindingResult.hasErrors()) {
                return "user/edit";
            }
            if (formUser.getPassword().equals("")) {
                formUser.setPassword(user.getPassword());
            }
            formUser.setRole(userService.findUserById(formUser.getId()).getRole());
            customJdbcUserDetailsManager.updateUser(formUser, principal);

            redirectAttributes.addFlashAttribute("message", "Utente aggiornato correttamente!");
            redirectAttributes.addFlashAttribute("messageClass", "alert-success");
            return "redirect:/index";
        }
    }

    @GetMapping("/createUser")
    public String addUser(Model model) {
        model.addAttribute("currentPage", "/create_User");
        User user = new User();
        UserStatus userStatus = userStatusRepository.findByUserStatusType(UserStatusType.DISPONIBILE);
        user.setUserStatus(userStatus);
        model.addAttribute("user", user);
        model.addAttribute("roles", authoritiesService.getAllAuthoritiesTypes());
        return "user/create";
    }

    @PostMapping("/createUser")
    public String addUser(@Valid @ModelAttribute("user") User formUser,
            BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {
        if (userRepository.existsByUsername(formUser.getUsername())) {
            bindingResult.rejectValue("username", "duplicate", "Username già esistente.");
        }

        if (userRepository.existsByEmail(formUser.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "Email già esistente.");
        }

        if (formUser.getPassword() == null || formUser.getPassword().trim().isEmpty()) {
            bindingResult.rejectValue("password", "error.user", "La password non può essere vuota.");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", authoritiesService.getAllAuthoritiesTypes());
            return "user/create";
        }

        UserStatus userStatus = userStatusRepository.findByUserStatusType(UserStatusType.DISPONIBILE);
        formUser.setUserStatus(userStatus);
        customJdbcUserDetailsManager.create(formUser);

        redirectAttributes.addFlashAttribute("message",
                "L'operatore " + formUser.getUsername() + " è stato eliminato correttamente!");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");

        return "redirect:/operatori";
    }

    @PostMapping("/setOperatoreNonDisponibile/{id}")
    public String setOperatoreNonDisponibile(@PathVariable Integer id, RedirectAttributes redirectAttributes,
            Principal principal) {
        User user = userService.findUserById(id);
        List<Ticket> tickets = ticketService.getTicketsByUserId(id);
        for (Ticket ticket : tickets) {
            System.out.println(ticket.getStatus().getStatus());
            if (ticket.getStatus().getStatus().equals("APERTO")
                    || ticket.getStatus().getStatus().equals("IN_CORSO")) {
                redirectAttributes.addFlashAttribute(
                        "Lo status non può essere modificato, ci sono ancora ticket aperti o in corso");
                redirectAttributes.addFlashAttribute("messageClass", "alert-danger");

                return "redirect:/index";
            }
        }
        user.setUserStatus(userStatusRepository.findByUserStatusType(UserStatusType.NON_DISPONIBILE));
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("Status modificato con successo.");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");
        return "redirect:/index";
    }

    @PostMapping("/setOperatoreDisponibile/{id}")
    public String setOperatoreDisponibile(@PathVariable Integer id, RedirectAttributes redirectAttributes,
            Principal principal) {
        User user = userService.findUserById(id);
        UserStatus userStatus = userStatusRepository.findByUserStatusType(UserStatusType.DISPONIBILE);
        user.setUserStatus(userStatus);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("Status modificato con successo.");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");
        return "redirect:/index";
    }

    @PostMapping("/deleteUser/{id}")
    public String postMethodName(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        String temp = userService.findUserById(id).getUsername();
        List<Ticket> tickets = ticketService.getTicketsByUserId(id);

        try {
            User nonAssegnati = userService.findByUsernameUser("Non Assegnati");
            for (int i = 0; i < tickets.size(); i++) {
                tickets.get(i).setOperatore(nonAssegnati);
                ticketService.saveTicket(tickets.get(i));
            }
            userService.deleteUser(userService.findUserById(id).getUsername());
            redirectAttributes.addFlashAttribute("message",
                    "L'operatore " + temp + " è stato eliminato correttamente!");
            redirectAttributes.addFlashAttribute("messageClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message",
                    "Si è verificato un errore durante l'eliminazione dell'operatore.");
            redirectAttributes.addFlashAttribute("messageClass", "alert-danger");
        }

        return "redirect:/operatori";
    }
}
