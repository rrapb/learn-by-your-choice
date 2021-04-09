package com.ubt.gymmanagementsystem.controllers.auth;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import com.ubt.gymmanagementsystem.entities.administration.User;
import com.ubt.gymmanagementsystem.repositories.administration.UserRepository;
import com.ubt.gymmanagementsystem.services.administration.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/403")
    public String forbidden403(){
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

    @GetMapping("/welcome/{token}")
    public String welcome(HttpServletResponse response, @PathVariable("token") String token, Model model) throws IOException {

        byte[] decodedBytes = Base64.getDecoder().decode(token);
        String tempPassword = new String(decodedBytes);

        model.addAttribute("password", tempPassword);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken))
            response.sendRedirect("/index");

        model.addAttribute("toLogout", true);
        return "auth/reset-password";
    }

    @GetMapping("/resetPassword")
    public String resetPasswordForm(@PathParam("error") boolean error, Model model){
        if(error)
            model.addAttribute("error", error);
        return "auth/reset-password-form";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("email") String email, @RequestParam("password") String password, Model model){

        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser") && StringUtils.isNotBlank(email)
                && StringUtils.isNotBlank(password)) {
            try {
                UserDetails userDetails = userService.loadUserByEmailAndPassword(email, password);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    return "auth/reset-password-form";
                }
            }catch (UsernameNotFoundException ex) {
                model.addAttribute("error", true);
                return "auth/reset-password";
            }
        }
        model.addAttribute("error", true);
        return "auth/reset-password";
    }

    @PostMapping("/resetPasswordForm")
    public void resetPasswordForm(HttpServletResponse response, @RequestParam("password") String password,
                                    @RequestParam("retypedPassword") String retypedPassword) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && StringUtils.isNotBlank(retypedPassword) && StringUtils.isNotBlank(retypedPassword)
                && password.equals(retypedPassword)) {

            String email = ((User)authentication.getPrincipal()).getEmail();
            userService.updatePassword(email, password);
            userService.activateUser(email);
            response.sendRedirect("/logout");
        }else {
            response.sendRedirect("/resetPassword?error=true");
        }
    }

    @GetMapping("/index")
    public String index(){
        return "fragments/dashboard";
    }
}