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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.Articolo;
import com.example.ticket_platform.model.Magazzino;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.dto.TempUser;
import com.example.ticket_platform.repository.ArticoloRepository;
import com.example.ticket_platform.repository.MagazzinoRepository;
import com.example.ticket_platform.service.MagazzinoService;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MagazzinoController {

    @Autowired
    UtilityFunctions utilityFunctions;
    @Autowired
    MagazzinoRepository magazzinoRepository;
    @Autowired
    MagazzinoService magazzinoService;
    @Autowired
    ArticoloRepository articoloRepository;

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

    @GetMapping("/magazzino")
    public String addNote(Model model,
            RedirectAttributes redirectAttributes, Principal principal) {

        Magazzino magazzino = magazzinoRepository.findByName("Sede").get();

        if (magazzino == null) {
            redirectAttributes.addFlashAttribute("message",
                    "Non è stato possibile trovare il magazzino");
            redirectAttributes.addFlashAttribute("messageClass", "alert-danger");
            return "redirect:/magazzino";
        }

        List<Articolo> articoli = magazzino.getArticoli();

        model.addAttribute("articoli", articoli);
        return "magazzino/index";
    }

}
