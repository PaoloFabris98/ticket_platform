package com.example.ticket_platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {

    @GetMapping("/not_autenticated")
    public String accessDeniedNotAutenticated() {
        return "accessDenied/accessDeniedNotAutenticated";
    }

    @GetMapping("/permissions_missing")
    public String accessDeniedAutenticated() {
        return "accessDenied/accessDeniedAutenticated";
    }

    @GetMapping("/ticket_Index_Out_Of_Bound")
    public String ticketIndexOutOfBound() {
        return "accessDenied/ticketIndexOutOfBound";
    }

    @GetMapping("/user_Index_Out_Of_Bound")
    public String userIndexOutOfBound() {
        return "accessDenied/userIndexOutOfBound";
    }

}
