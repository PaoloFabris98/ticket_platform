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
import com.example.ticket_platform.model.Categoria;
import com.example.ticket_platform.model.Descrizione;
import com.example.ticket_platform.model.Nota;
import com.example.ticket_platform.model.Note;
import com.example.ticket_platform.model.Status;
import com.example.ticket_platform.model.StatusType;
import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.UserStatusType;
import com.example.ticket_platform.repository.CategoriaRepository;
import com.example.ticket_platform.repository.StatusRepository;
import com.example.ticket_platform.repository.TicketRepository;
import com.example.ticket_platform.repository.UserRepository;
import com.example.ticket_platform.service.*;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private StatusService statusService;
    @Autowired
    private UtilityFunctions utilityFunctions;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private NotaService notaService;
    @Autowired
    private UserRepository userRepository;

    TicketController(NotaService notaService) {
        this.notaService = notaService;
    }

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

    @GetMapping("/ticket/{id}")
    public String getMethodName(@PathVariable Integer id, Model model, Principal principal,
            RedirectAttributes redirectAttributes) {
        Ticket ticket = ticketService.getTicketById(id);
        if (utilityFunctions.isAdmin(utilityFunctions.currentUser(principal))) {
            model.addAttribute("ticket", ticket);
            return "ticket/ticket";
        } else {
            if (ticket.getOperatore().getUsername().equals(utilityFunctions.currentUser(principal).getUsername())) {
                model.addAttribute(ticket);
                return "ticket/ticket";
            } else {
                return "redirect:/permissions_missing";
            }
        }

    }

    @GetMapping("/editTicket/{id}")
    public String editTicket(@PathVariable Integer id, Model model) {
        Ticket ticket = ticketService.getTicketById(id);
        List<User> users = userService.getAll();
        List<StatusType> statusTypes = statusService.getAllStatusTypes();
        model.addAttribute("ticket", ticket);
        model.addAttribute("users", users);
        model.addAttribute("statusType", statusTypes);
        model.addAttribute("categorie", categoriaService.getAllCategoriaStatusTypes());
        return "ticket/edit";
    }

    @PostMapping("/editTicket/{id}")
    public String editTicket(@Valid @ModelAttribute("ticket") Ticket formTicket,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ticket/edit";
        }
        Status status = statusRepository.findByStatus(formTicket.getStatus().getStatus());
        Categoria categoria = categoriaRepository.findByNome(formTicket.getCategoria().getNome());
        formTicket.setStatus(status);
        formTicket.setCategoria(categoria);
        ticketService.updateTicket(formTicket);

        redirectAttributes.addFlashAttribute("message", "Il ticket è stato modificato con successo");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");
        return "redirect:/index";
    }

    @GetMapping("/addTicket")
    public String addTicket(Model model) {
        Ticket ticket = new Ticket();
        ticket.setStatus(statusRepository.findByStatus(StatusType.APERTO));
        ticket.setDataCreazione(LocalDate.now());
        model.addAttribute("statusList", statusService.getAllStatusTypes());
        model.addAttribute("categorie", categoriaService.getAllCategoriaStatusTypes());
        model.addAttribute("users", userService.finaAllByStatus());
        model.addAttribute("ticket", ticket);
        return "ticket/add";
    }

    @PostMapping("/addTicket")
    public String addTicket(@Valid @ModelAttribute("ticket") Ticket formTicket,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ticket/add";
        }
        Status status = statusRepository.findByStatus(formTicket.getStatus().getStatus());
        Categoria categoria = categoriaRepository.findByNome(formTicket.getCategoria().getNome());
        formTicket.setStatus(status);
        formTicket.setCategoria(categoria);
        ticketService.saveTicket(formTicket);

        redirectAttributes.addFlashAttribute("message", "Il ticket è stato creato con successo");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");

        return "redirect:/index";
    }

    @GetMapping("/editDescrizione/{id}")
    public String editDescrizione(@PathVariable Integer id, Model model) {
        Descrizione descrizione = new Descrizione();
        descrizione.setDescrizione(ticketService.getTicketById(id).getDescrizione());
        model.addAttribute("descrizione", descrizione);
        model.addAttribute("id", id);
        return "ticket/addDescrizione";
    }

    @PostMapping("/editDescrizione/{id}")
    public String editDescrizione(@PathVariable("id") Integer id,
            @Valid @ModelAttribute("descrizione") Descrizione formDescrizione,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ticket/addDescrizione";
        }

        Ticket ticket = ticketRepository.findById(id).get();
        ticket.setDescrizione(formDescrizione.getDescrizione());
        ticketService.updateTicket(ticket);

        redirectAttributes.addFlashAttribute("message", "Descrizione modificata con successo");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");

        return "redirect:/index";
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

    @PostMapping("/setStatusIn_corso/{id}")
    public String setStatusIn_corso(@PathVariable Integer id) {
        Ticket ticket = ticketService.getTicketById(id);
        ticket.setStatus(statusRepository.findByStatus(StatusType.IN_CORSO));
        ticketService.updateTicket(ticket);
        return "redirect:/index";
    }

    @PostMapping("/setStatusChiuso/{id}")
    public String setStatusChiuso(@PathVariable Integer id) {
        Ticket ticket = ticketService.getTicketById(id);
        ticket.setStatus(statusRepository.findByStatus(StatusType.CHIUSO));
        ticket.setDataChiusura(LocalDate.now());
        ticketService.updateTicket(ticket);
        return "redirect:/index";
    }

    @PostMapping("/setStatusAperto/{id}")
    public String setStatusAperto(@PathVariable Integer id) {
        Ticket ticket = ticketService.getTicketById(id);
        ticket.setStatus(statusRepository.findByStatus(StatusType.APERTO));
        ticketService.updateTicket(ticket);
        return "redirect:/index";
    }

    @PostMapping("/deleteTicket/{id}")
    public String deleteTicket(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        ticketRepository.delete(ticketService.getTicketById(id));
        redirectAttributes.addFlashAttribute("message", "Il ticket è stato cancellato con successo");
        redirectAttributes.addFlashAttribute("messageClass", "alert-danger");
        return "redirect:/index";
    }

}
