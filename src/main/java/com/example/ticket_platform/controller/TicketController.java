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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.Categoria;
import com.example.ticket_platform.model.Allegato;
import com.example.ticket_platform.model.Status;
import com.example.ticket_platform.model.StatusType;
import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.dto.TempUser;
import com.example.ticket_platform.repository.CategoriaRepository;
import com.example.ticket_platform.repository.ClienteRepository;
import com.example.ticket_platform.repository.AllegatoRepository;
import com.example.ticket_platform.repository.StatusRepository;
import com.example.ticket_platform.repository.TicketRepository;
import com.example.ticket_platform.service.*;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestBody;

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
    private ClienteRepository clienteRepository;
    @Autowired
    private AllegatoRepository allegatoRepository;

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

    @GetMapping("/ticket/{id}")
    public String getMethodName(@PathVariable Integer id, Model model, Principal principal,
            RedirectAttributes redirectAttributes) {
        Ticket ticket = ticketService.getTicketById(id);
        if (ticket == null) {
            return "redirect:/ticket_Index_Out_Of_Bound";
        }
        model.addAttribute("allegati", ticket.getImgs());
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
        if (ticket == null) {
            return "redirect:/ticket_Index_Out_Of_Bound";
        }
        List<StatusType> statusTypes = statusService.getAllStatusTypes();
        model.addAttribute("ticket", ticket);
        model.addAttribute("users", userService.finaAllByStatus());
        model.addAttribute("statusType", statusTypes);
        model.addAttribute("categorie", categoriaService.getAllCategoriaStatusTypes());
        model.addAttribute("clienti", clienteRepository.findAll());

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
        model.addAttribute("currentPage", "/addTicket");
        Ticket ticket = new Ticket();
        ticket.setStatus(statusRepository.findByStatus("APERTO"));
        ticket.setDataCreazione(LocalDate.now());
        model.addAttribute("statusList", statusService.getAllStatusTypes());
        model.addAttribute("categorie", categoriaService.getAllCategoriaStatusTypes());
        model.addAttribute("users", userService.finaAllByStatus());
        model.addAttribute("ticket", ticket);
        model.addAttribute("clienti", clienteRepository.findAll());
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

    @PostMapping("/setStatusIn_corso/{id}")
    public String setStatusIn_corso(@PathVariable Integer id) {
        Ticket ticket = ticketService.getTicketById(id);
        ticket.setStatus(statusRepository.findByStatus("IN_CORSO"));
        ticketService.updateTicket(ticket);
        return "redirect:/index";
    }

    @PostMapping("/setStatusChiuso/{id}/articoli-usati")
    public String setStatusChiuso(@PathVariable Integer id) {
        Ticket ticket = ticketService.getTicketById(id);
        ticket.setStatus(statusRepository.findByStatus("CHIUSO"));
        ticket.setDataChiusura(LocalDate.now());
        ticketService.updateTicket(ticket);

        return "redirect:/index";
    }

    @PostMapping("/setStatusAperto/{id}")
    public String setStatusAperto(@PathVariable Integer id) {
        Ticket ticket = ticketService.getTicketById(id);
        ticket.setStatus(statusRepository.findByStatus("APERTO"));
        ticketService.updateTicket(ticket);
        return "redirect:/index";
    }

    @PostMapping("/deleteTicket/{id}")
    public String deleteTicket(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        ticketRepository.delete(ticketService.getTicketById(id));
        redirectAttributes.addFlashAttribute("message", "Il ticket è stato cancellato con successo");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");
        return "redirect:/index";
    }

    @PostMapping("/deleteTicketFile/{ticketId}/{fileId}")
    public String postMethodName(@PathVariable Integer ticketId, @PathVariable Integer fileId,
            RedirectAttributes redirectAttributes) throws IOException {

        Allegato allegato = allegatoRepository.findById(fileId).get();
        Path path = Paths.get(allegato.getImgPath());
        try {
            Files.delete(path);
            allegatoRepository.delete(allegato);
            redirectAttributes.addFlashAttribute("message", "Il file è stato cancellato con successo");
            redirectAttributes.addFlashAttribute("messageClass", "alert-success");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Errore durante la cancellazione del file");
            redirectAttributes.addFlashAttribute("messageClass", "alert-danger");
            e.printStackTrace();
        }

        return "redirect:/ticket/" + ticketId;

    }

}
