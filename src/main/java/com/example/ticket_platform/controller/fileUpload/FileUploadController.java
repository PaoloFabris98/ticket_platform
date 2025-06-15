package com.example.ticket_platform.controller.fileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

import com.example.ticket_platform.service.TicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.Img;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.dto.TempUser;
import com.example.ticket_platform.repository.ImgRepository;

import org.springframework.ui.Model;

@Controller
public class FileUploadController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UtilityFunctions utilityFunctions;

    @Autowired
    private ImgRepository imgRepository;

    @Value("${upload.dir}")
    private String uploadDir;

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
        return tempUser;
    }

    @PostMapping("/upload/{id}")
    public String handleFileUpload(@org.springframework.web.bind.annotation.RequestParam("file") MultipartFile file,
            @PathVariable Integer id,
            Model model, RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Nessun file selezionato");
            redirectAttributes.addFlashAttribute("messageClass", "alert-warning");
            return "redirect:/ticket/" + ticketService.getTicketById(id).getId();
        }
        try {
            Path uploadPath = Paths.get(System.getProperty("user.dir"), uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(file.getOriginalFilename());

            Img img = new Img();
            img.setImgPath(filePath.toString());
            img.setTicket(ticketService.getTicketById(id));
            img.setFileName(file.getOriginalFilename());

            file.transferTo(filePath.toFile());
            imgRepository.save(img);

            redirectAttributes.addFlashAttribute("message",
                    "File caricato con successo: " + file.getOriginalFilename());
            redirectAttributes.addFlashAttribute("messageClass", "alert-success");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Errore durante il caricamento: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageClass", "alert-danger");
        }

        return "redirect:/ticket/" + ticketService.getTicketById(id).getId();
    }
}
