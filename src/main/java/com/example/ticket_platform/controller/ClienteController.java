package com.example.ticket_platform.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.Cliente;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.repository.ClienteRepository;
import com.example.ticket_platform.service.ClienteService;

@Controller
public class ClienteController {

    @Autowired
    private UtilityFunctions utilityFunctions;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ClienteService clienteService;

    @ModelAttribute("currentUser")
    public String getCurrentUser(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return utilityFunctions.currentUser(principal).getUsername();
        }
        return "redirect:/login";
    }

    @ModelAttribute("currentUserObj")
    public User getCurrentUserObj(Principal principal) {
        return utilityFunctions.currentUser(principal);

    }

    @GetMapping("/clienti")
    public String clienti(Model model) {
        List<Cliente> clienti = clienteRepository.findAll();
        model.addAttribute("clienti", clienti);
        return "cliente/index";
    }

    @PostMapping("/deleteCliente/{id}")
    public String postMethodName(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        String temp = clienteService.findById(id).get().getNome();
        clienteRepository.delete(clienteService.findById(id).get());

        redirectAttributes.addFlashAttribute("message", "Il cliente " + temp + " è stato eliminato correttamente!");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");

        return "redirect:/clienti";
    }
}
