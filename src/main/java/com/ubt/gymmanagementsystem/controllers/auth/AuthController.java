package com.ubt.gymmanagementsystem.controllers.auth;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AuthController {

    @GetMapping("/403")
    public String forbidden403(Model model){
        return "auth/403";
    }

    @GetMapping("/404")
    public String notFound404(){
        return "auth/404";
    }

    @GetMapping("/login")
    public String login(HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken))
            response.sendRedirect("/index");

        return "auth/login";
    }

    @GetMapping("/index")
    public String index(Model model){
//        model.addAttribute("template", "");
        return "fragments/dashboard";
    }
}