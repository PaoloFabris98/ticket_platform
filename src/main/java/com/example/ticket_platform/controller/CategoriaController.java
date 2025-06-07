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

import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.Categoria;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.repository.CategoriaRepository;
import com.example.ticket_platform.service.CategoriaService;

@Controller
public class CategoriaController {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private CategoriaService categoriaService;
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
    public User getCurrentUserObj(Principal principal) {
        return utilityFunctions.currentUser(principal);

    }

    @GetMapping("/categoria_ticket")
    public String seeOperators(Model model) {
        List<Categoria> categorie = categoriaRepository.findAll();
        model.addAttribute("categorie", categorie);
        return "categoria/index";
    }

}
