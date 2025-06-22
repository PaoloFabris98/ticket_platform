package com.example.ticket_platform.controller.fileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

import com.example.ticket_platform.service.TicketService;
import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.Allegato;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.dto.TempUser;
import com.example.ticket_platform.repository.AllegatoRepository;
import com.example.ticket_platform.repository.OptionsRepository;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileUploadController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UtilityFunctions utilityFunctions;

    @Autowired
    private AllegatoRepository fileRepository;

    @Autowired
    private OptionsRepository optionsRepository;

    @Value("${upload.ticket.dir}")
    private String ticketUploadDir;

    @Value("${upload.manual.dir}")
    private String manualUploadDir;

    private Path uploadPath;

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

    @PostMapping("/upload/{id}/{destination}")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            @PathVariable Integer id, @PathVariable String destination,
            Model model, RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Nessun file selezionato");
            redirectAttributes.addFlashAttribute("messageClass", "alert-warning");
            return "redirect:/ticket/" + id;
        }

        try {
            if (destination.equals("ticket")) {
                uploadPath = Paths.get(System.getProperty("user.dir"),
                        optionsRepository.findAll().getFirst().getTicketUploadDir());
            } else if (destination.equals("manuali")) {
                uploadPath = Paths.get(System.getProperty("user.dir"),
                        optionsRepository.findAll().getFirst().getManualsUploadDir());
            }

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String baseName = FilenameUtils.getBaseName(originalFileName);
            String extension = FilenameUtils.getExtension(originalFileName);

            String finalFileName = originalFileName;
            Path filePath = uploadPath.resolve(finalFileName);

            int counter = 1;
            while (Files.exists(filePath)) {
                finalFileName = baseName + "(" + counter + ")." + extension;
                filePath = uploadPath.resolve(finalFileName);
                counter++;
            }

            file.transferTo(filePath.toFile());

            Allegato fileOBJ = new Allegato();
            fileOBJ.setImgPath(filePath.toString());
            fileOBJ.setFileName(finalFileName);
            fileOBJ.setTicket(ticketService.getTicketById(id));
            fileRepository.save(fileOBJ);

            redirectAttributes.addFlashAttribute("message", "File caricato con successo: " + originalFileName);
            redirectAttributes.addFlashAttribute("messageClass", "alert-success");

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Errore durante il caricamento: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageClass", "alert-danger");
        }

        return "redirect:/ticket/" + id;
    }
}
