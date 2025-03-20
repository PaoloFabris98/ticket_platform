package com.example.ticket_platform.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ticket_platform.component.UtilityFunctions;
import com.example.ticket_platform.model.ApiUser;
import com.example.ticket_platform.model.User;

import com.example.ticket_platform.repository.ApiUserRepository;
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

    @GetMapping("/API")
    public String index(Model model) {
        List<ApiUser> apiUsers = apiUserRepository.findAll();
        model.addAttribute("apiUsers", apiUsers);
        return "api/index";
    }

    @GetMapping("/addAPI")
    public String addAPI(Model model) {
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
}
