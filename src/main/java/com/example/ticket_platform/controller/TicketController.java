package com.example.ticket_platform.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.Categoria;
import com.example.ticket_platform.model.Magazzino;
import com.example.ticket_platform.model.Allegato;
import com.example.ticket_platform.model.Articolo;
import com.example.ticket_platform.model.ArticoloUtilizzato;
import com.example.ticket_platform.model.Status;
import com.example.ticket_platform.model.StatusType;
import com.example.ticket_platform.model.Ticket;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.dto.ArticoliUsatiDTO;
import com.example.ticket_platform.model.dto.TempUser;
import com.example.ticket_platform.model.wrapper.ArticoliUsatiWrapper;
import com.example.ticket_platform.repository.CategoriaRepository;
import com.example.ticket_platform.repository.ClienteRepository;
import com.example.ticket_platform.repository.MagazzinoRepository;
import com.example.ticket_platform.repository.AllegatoRepository;
import com.example.ticket_platform.repository.ArticoloRepository;
import com.example.ticket_platform.repository.ArticoloUtilizzatoRepository;
import com.example.ticket_platform.repository.StatusRepository;
import com.example.ticket_platform.repository.TicketRepository;
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
    private ClienteRepository clienteRepository;
    @Autowired
    private AllegatoRepository allegatoRepository;
    @Autowired
    private ArticoloRepository articoloRepository;
    @Autowired
    private ArticoloUtilizzatoRepository articoloUtilizzatoRepository;
    @Autowired
    private MagazzinoRepository magazzinoRepository;

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

        Magazzino magazzino = magazzinoRepository.findByProprietario(ticket.getOperatore()).get();

        List<ArticoliUsatiDTO> articoliUsati = new ArrayList<>();

        for (Articolo articolo : magazzino.getArticoli()) {
            ArticoliUsatiDTO articoliUsatiDTO = new ArticoliUsatiDTO();
            articoliUsatiDTO.setId(articolo.getId());
            articoliUsatiDTO.setArticolo(articolo);
            articoliUsatiDTO.setId(articolo.getId());
            if (ticket.getOperatore().getVanKit()
                    .getArticoloByName(articolo.getName()).getQuantità() == 0
                    || ticket.getOperatore().getVanKit()
                            .getArticoloById(articolo.getId()).getQuantità() == null) {
                articoliUsatiDTO.setQuantità(0);
            } else {
                articoliUsatiDTO.setQuantità(ticket.getOperatore().getVanKit()
                        .getArticoloById(articolo.getId()).getQuantità());
            }
            articoliUsati.add(articoliUsatiDTO);
        }

        ArticoliUsatiWrapper articoliUsatiWrapper = new ArticoliUsatiWrapper();
        articoliUsatiWrapper.setArticoliUsati(articoliUsati);

        model.addAttribute("allegati", ticket.getImgs());
        if (utilityFunctions.isAdmin(utilityFunctions.currentUser(principal))) {
            model.addAttribute("ticket", ticket);
            model.addAttribute("magazzino", magazzino.getProprietario().getUsername());
            model.addAttribute("articoliUsatiWrapper", articoliUsatiWrapper);
            return "ticket/ticket";
        } else {
            if (ticket.getOperatore().getUsername().equals(utilityFunctions.currentUser(principal).getUsername())) {
                model.addAttribute(ticket);
                model.addAttribute("articoliUsatiWrapper", articoliUsatiWrapper);
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
    public String setStatusChiuso(@PathVariable Integer id,
            @Valid @ModelAttribute("articoliUsatiWrapper") ArticoliUsatiWrapper wrapper) {
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

    @PostMapping("/saveArticles/{id}/articoli-usati")
    public String addArticolo(@PathVariable("id") Integer id,
            @ModelAttribute("articoliUsatiWrapper") ArticoliUsatiWrapper articoliUsatiWrapper,
            RedirectAttributes redirectAttributes) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (!ticket.isPresent()) {
            redirectAttributes.addFlashAttribute("message", "Non è stato possibile trovare il ticket.");
            redirectAttributes.addFlashAttribute("messageClass", "alert-danger");
            return "redirect:/ticket/" + id;
        }
        if (articoliUsatiWrapper == null) {
            redirectAttributes.addFlashAttribute("message", "Non è stato possibile trovare gli articoli utilizzati.");
            redirectAttributes.addFlashAttribute("messageClass", "alert-danger");
            return "redirect:/ticket/" + id;
        }
        if (articoliUsatiWrapper.getListLenght() == 0 || articoliUsatiWrapper.getListLenght() == 0) {
            redirectAttributes.addFlashAttribute("message",
                    "Non è stato possibile trovare gli articoli nell'inventario dell'operatore.");
            redirectAttributes.addFlashAttribute("messageClass", "alert-danger");
            return "redirect:/ticket/" + id;
        }

        User operatore = userService.findByUsernameUser(ticket.get().getOperatoreUsername());
        Magazzino vankitOperatore = magazzinoRepository.findByProprietario(operatore).get();

        for (ArticoliUsatiDTO articoliUsatiDTO : articoliUsatiWrapper.getArticoliUsati()) {
            Articolo currentArticle = vankitOperatore.getArticoloById(articoliUsatiDTO.getId());

            if (currentArticle == null) {
                redirectAttributes.addFlashAttribute("message",
                        "Articolo con ID " + articoliUsatiDTO.getArticolo().getName()
                                + " non trovato nel magazzino dell'operatore.");
                redirectAttributes.addFlashAttribute("messageClass", "alert-danger");
                return "redirect:/ticket/" + id;
            }

            if (articoliUsatiDTO.getQuantitàUsata() == null) {
                articoliUsatiDTO.setQuantitàUsata(0);
            }

            if (articoliUsatiDTO.getQuantitàUsata() < 0) {
                redirectAttributes.addFlashAttribute("message",
                        "La quantità utilizzata dell'articolo non può essere inferiore a 0.");
                redirectAttributes.addFlashAttribute("messageClass", "alert-danger");
                return "redirect:/ticket/" + id;
            }

            currentArticle.addQuantity((articoliUsatiDTO.getQuantitàUsata()) * -1);

            if (currentArticle.getQuantità() < 0) {
                redirectAttributes.addFlashAttribute("message",
                        "Controlla le quantità nel magazzino, non puoi usare più articoli di quanti tu ne abbia a disposizione.");
                redirectAttributes.addFlashAttribute("messageClass", "alert-danger");
                return "redirect:/ticket/" + id;
            }

            ArticoloUtilizzato articoloUtilizzato = new ArticoloUtilizzato();
            articoloUtilizzato.setArticolo(currentArticle);
            articoloUtilizzato.setTicket(ticket.get());
            articoloUtilizzato.setQuantità(articoliUsatiDTO.getQuantitàUsata());
            if (!(articoliUsatiDTO.getQuantitàUsata() == 0)) {
                articoloUtilizzatoRepository.save(articoloUtilizzato);
            }

            articoloRepository.save(currentArticle);
        }

        redirectAttributes.addFlashAttribute("message",
                "Articoli salvati correttamente.");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");
        return "redirect:/ticket/" + id;

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
