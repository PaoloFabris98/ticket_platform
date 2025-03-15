package com.example.ticket_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.UserStatus;
import com.example.ticket_platform.model.UserStatusType;
import com.example.ticket_platform.repository.AuthoritiesRepository;
import com.example.ticket_platform.repository.UserRepository;
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

    @Autowired
    private UserService userService;
    @Autowired
    private CustomJdbcUserDetailsManager customJdbcUserDetailsManager;
    @Autowired
    AuthoritiesRepository authoritiesRepository;
    @Autowired
    UserStatusRepository userStatusRepository;

    @GetMapping("/operatori")
    public String seeOperators(Model model) {
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
        return "user/index";
    }

    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable Integer id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "user/edit";
    }

    @PostMapping("/editUser/{id}")
    public String editUser(@Valid @ModelAttribute("user") User formUser,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "user/edit";
        }
        customJdbcUserDetailsManager.updateUser(formUser);
        redirectAttributes.addFlashAttribute("message", "Utente aggiornato correttamente!");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");
        return "redirect:/operatori";
    }

    @GetMapping("/createUser")
    public String addUser(Model model) {
        User user = new User();
        UserStatus userStatus = userStatusRepository.findByUserStatusType(UserStatusType.ATTIVO);
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

        UserStatus userStatus = userStatusRepository.findByUserStatusType(UserStatusType.ATTIVO);
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
