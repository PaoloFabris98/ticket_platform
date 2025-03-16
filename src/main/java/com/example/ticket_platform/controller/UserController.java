package com.example.ticket_platform.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.UserStatus;
import com.example.ticket_platform.model.UserStatusType;
import com.example.ticket_platform.repository.UserStatusRepository;
import com.example.ticket_platform.security.CustomJdbcUserDetailsManager;
import com.example.ticket_platform.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UtilityFunctions utilityFunctions;

    @Autowired
    private UserService userService;
    @Autowired
    private CustomJdbcUserDetailsManager customJdbcUserDetailsManager;
    @Autowired
    private UserStatusRepository userStatusRepository;

    UserController(UtilityFunctions utilityFunctions) {
        this.utilityFunctions = utilityFunctions;
    }

    @GetMapping("/operatori")
    public String seeOperators(Model model) {
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
        return "user/index";
    }

    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable Integer id, Model model, Principal principal) {
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
        return "user/edit";
    }

    @PostMapping("/editUser/{id}")
    public String editUser(@Valid @ModelAttribute("user") User formUser,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes, Principal principal) {

        if (bindingResult.hasErrors()) {
            return "user/edit";
        }
        String oldUsername = formUser.getUsername();
        customJdbcUserDetailsManager.updateUser(formUser);

        if (!oldUsername.equals(formUser.getUsername())) {
            customJdbcUserDetailsManager.updateUsernameReferences(oldUsername, formUser);
        }

        redirectAttributes.addFlashAttribute("message", "Utente aggiornato correttamente!");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");
        if (utilityFunctions.isAdmin(utilityFunctions.currentUser(principal))) {
            return "redirect:/operatori";
        } else {
            return "redirect:/index";
        }
    }

    @GetMapping("/createUser")
    public String addUser(Model model) {
        User user = new User();
        UserStatus userStatus = userStatusRepository.findByUserStatusType(UserStatusType.DISPONIBILE);
        user.setUserStatus(userStatus);
        model.addAttribute("user", user);
        return "user/create";
    }

    @PostMapping("/createUser")
    public String addUser(@Valid @ModelAttribute("user") User formUser,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
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

    @PostMapping("/deleteUser/{id}")
    public String postMethodName(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        String temp = userService.findUserById(id).getUsername();
        userService.deleteUser(userService.findUserById(id).getUsername());

        redirectAttributes.addFlashAttribute("message", "L'operatore " + temp + " è stato eliminato correttamente!");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");

        return "redirect:/operatori";
    }

}
