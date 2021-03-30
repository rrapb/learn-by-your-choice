package com.ubt.gymmanagementsystem.controllers.administration;

import com.ubt.gymmanagementsystem.services.administration.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    @PostAuthorize("hasAuthority('READ_USER')")
    public String users(Model model){
        model.addAttribute("users", userService.getAll());
        return "administration/users/users";
    }
}