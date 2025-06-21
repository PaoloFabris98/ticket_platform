package com.example.ticket_platform.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.Cliente;
import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.dto.TempUser;
import com.example.ticket_platform.repository.ClienteRepository;
import com.example.ticket_platform.repository.TicketRepository;
import com.example.ticket_platform.service.ClienteService;

import jakarta.validation.Valid;

@Controller
public class ClienteController {

    @Autowired
    private UtilityFunctions utilityFunctions;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private TicketRepository ticketRepository;

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

    @GetMapping("/clienti")
    public String clienti(Model model) {
        model.addAttribute("currentPage", "/clienti");
        List<Cliente> clienti = clienteRepository.findAll();
        model.addAttribute("clienti", clienti);
        return "cliente/index";
    }

    @GetMapping("/addCliente")
    public String addCliente(Model model) {
        model.addAttribute("currentPage", "/addCliente");
        Cliente cliente = new Cliente();
        model.addAttribute("cliente", cliente);
        return "cliente/add";
    }

    @PostMapping("/addCliente")
    public String addCliente(@Valid @ModelAttribute("cliente") Cliente formCliente, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "cliente/add";
        }
        if (clienteRepository.existsByNome(formCliente.getNome())) {
            bindingResult.rejectValue("nome", "duplicate", "Il nome è già esistente.");
        }
        if (clienteRepository.existsByEmail(formCliente.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "L'email è già esistente.");
        }
        if (clienteRepository.existsByCellulare(formCliente.getCellulare())) {
            bindingResult.rejectValue("cellulare", "duplicate", "Il numero di cellulare è già esistente.");
        }
        if (bindingResult.hasErrors()) {
            return "cliente/add";
        }
        clienteRepository.save(formCliente);
        redirectAttributes.addFlashAttribute("message",
                "Il cliente " + formCliente.getNome() + " è stato aggiunto correttamente!");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");
        return "redirect:/clienti";
    }

    @GetMapping("/editCliente/{id}")
    public String editCliente(@PathVariable Integer id, Model model) {
        model.addAttribute("cliente", clienteRepository.findById(id).get());
        return "cliente/edit";
    }

    @PostMapping("/editCliente/{id}")
    public String editCliente(@Valid @ModelAttribute("cliente") Cliente formCliente, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "cliente/edit";
        }
        if (clienteRepository.existsByNome(formCliente.getNome())) {
            bindingResult.rejectValue("nome", "duplicate", "Il nome è già esistente.");
        }
        if (clienteRepository.existsByEmail(formCliente.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "L'email è già esistente.");
        }
        if (clienteRepository.existsByCellulare(formCliente.getCellulare())) {
            bindingResult.rejectValue("cellulare", "duplicate", "Il numero di cellulare è già esistente.");
        }

        if (bindingResult.hasErrors()) {
            return "cliente/edit";
        }

        List<Ticket> tickets = ticketRepository.findByCliente(clienteRepository.findById(formCliente.getId()).get());
        formCliente.setTickets(tickets);

        clienteRepository.save(formCliente);

        redirectAttributes.addFlashAttribute("message",
                "Il cliente " + formCliente.getNome() + " è stato modificato correttamente!");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");
        return "redirect:/clienti";
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
