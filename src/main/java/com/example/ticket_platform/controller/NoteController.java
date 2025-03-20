package com.example.ticket_platform.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.Nota;
import com.example.ticket_platform.repository.NotaRepository;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {
    @Autowired
    private NotaRepository notaRepository;
    @Autowired
    private UtilityFunctions utilityFunctions;

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
