package com.example.ticket_platform.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.Nota;
import com.example.ticket_platform.model.Note;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.dto.TempUser;
import com.example.ticket_platform.repository.NotaRepository;
import com.example.ticket_platform.service.NotaService;
import com.example.ticket_platform.service.TicketService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {
    @Autowired
    private NotaRepository notaRepository;
    @Autowired
    private UtilityFunctions utilityFunctions;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private NotaService notaService;

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

    @GetMapping("/addNota/{id}")
    public String addNote(@PathVariable Integer id, Model model, Principal principal) {
        Note note = new Note();
        model.addAttribute("note", note);
        return "ticket/addNota";
    }

    @PostMapping("/addNota/{id}")
    public String addNote(@PathVariable("id") Integer id,
            @Valid @ModelAttribute("nota") Note formNota,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "ticket/addNota";
        }
        Nota nota = new Nota();
        User user = utilityFunctions.currentUser(principal);
        System.out.println(utilityFunctions.currentUser(principal).getUsername());
        nota.setDataCreazione(LocalDate.now());
        nota.setNota(formNota.getNote());
        nota.setTicket(ticketService.getTicketById(id));
        nota.setAutore(user);
        notaService.save(nota);
        redirectAttributes.addFlashAttribute("message", "Nota creata con successo");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");

        return "redirect:/index";
    }

    @PostMapping("/deleteNote/{id}")
    public String deleteNote(@PathVariable Integer id, RedirectAttributes redirectAttributes, Principal principal) {
        if (utilityFunctions.isAdmin(utilityFunctions.currentUser(principal))) {
            notaRepository.delete(notaRepository.findById(id).get());
            redirectAttributes.addFlashAttribute("message", "Nota cancellata con successo");
            redirectAttributes.addFlashAttribute("messageClass", "alert-success");
            return "redirect:/index";
        } else {
            List<Nota> note = notaRepository.findByAutore(utilityFunctions.currentUser(principal));
            for (Nota nota : note) {
                if (nota.getAutore().getUsername().equals(utilityFunctions.currentUser(principal).getUsername())) {
                    notaRepository.delete(notaRepository.findById(id).get());
                    redirectAttributes.addFlashAttribute("message", "Nota cancellata con successo");
                    redirectAttributes.addFlashAttribute("messageClass", "alert-success");

                    return "redirect:/index";
                } else {
                    redirectAttributes.addFlashAttribute("message",
                            "Non puoi cancellare una nota che non ti appartiene.");
                    redirectAttributes.addFlashAttribute("messageClass", "alert-success");
                    return "redirect:/index";
                }
            }
        }

        return "redirect:/Index";
    }

}
