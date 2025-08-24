package com.example.ticket_platform.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
import com.example.ticket_platform.model.ApiUser;
import com.example.ticket_platform.model.User;
import com.example.ticket_platform.model.dto.TempUser;
import com.example.ticket_platform.repository.ApiUserRepository;
import com.example.ticket_platform.repository.UserRepository;
import com.example.ticket_platform.service.ApiUserService;

import jakarta.validation.Valid;

@Controller
public class ApiUserController {

    @Autowired
    private ApiUserService apiUserService;
    @Autowired
    private UtilityFunctions utilityFunctions;
    @Autowired
    private ApiUserRepository apiUserRepository;
    @Autowired
    private UserRepository userRepository;

    @ModelAttribute("currentUser")
    public String getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return authentication.getName();
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

    @GetMapping("/API")
    public String index(Model model) {
        model.addAttribute("currentPage", "/API");
        List<ApiUser> apiUsers = apiUserRepository.findAll();
        model.addAttribute("apiUsers", apiUsers);
        return "api/index";
    }

    @GetMapping("/addAPI")
    public String addAPI(Model model) {
        model.addAttribute("currentPage", "/addAPI");
        ApiUser apiUser = new ApiUser();
        String key = utilityFunctions.authKeyGenerator(20);
        apiUser.setAuthKey(key);
        model.addAttribute("apiUser", apiUser);
        return "api/addAPI";
    }

    @PostMapping("/addAPI")
    public String addAPI(@Valid @ModelAttribute("apiUser") ApiUser createAPIUserForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            System.out.println(createAPIUserForm.getAuthKey());
            System.out.println(createAPIUserForm.getPassword());
            System.out.println(createAPIUserForm.getUsername());
            System.out.println(createAPIUserForm.getId());
            redirectAttributes.addFlashAttribute("message", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("messageClass", "alert-danger");
            return "api/addAPI";
        }

        System.out.println("Salvo l'ApiUser: " + createAPIUserForm);
        System.out.println("ApiUser salvato!");

        apiUserService.saveApiUser(createAPIUserForm);

        redirectAttributes.addFlashAttribute("message", "Lo User per il Rest è stato creato correttamente");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");

        return "redirect:/API";
    }

    @GetMapping("/editApi/{id}")
    public String editApi(@PathVariable Integer id, Model model) {
        model.addAttribute("api", apiUserRepository.findById(id).get());
        return "api/edit";
    }

    @PostMapping("/editApi/{id}")
    public String editApi(@PathVariable Integer id, @Valid @ModelAttribute("api") ApiUser editAPIUserForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (editAPIUserForm.getPassword().equals("")) {
            editAPIUserForm.setPassword(apiUserRepository.findById(id).get().getPassword());
        }
        if (bindingResult.hasErrors()) {
            return "api/edit";
        }

        apiUserService.saveApiUser(editAPIUserForm);

        redirectAttributes.addFlashAttribute("message", "Lo User per il Rest è stato creato correttamente");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");

        return "redirect:/API";
    }

    @PostMapping("/deleteApi/{id}")
    public String deleteApi(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        apiUserRepository.delete(apiUserRepository.findById(id).get());
        redirectAttributes.addFlashAttribute("message", "Lo User per il Rest è stato cancellato correttamente");
        redirectAttributes.addFlashAttribute("messageClass", "alert-success");
        return "redirect:/API";
    }
}
