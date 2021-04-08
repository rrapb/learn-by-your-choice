package com.ubt.gymmanagementsystem.controllers.administration;

import com.ubt.gymmanagementsystem.configurations.exceptions.DatabaseException;
import com.ubt.gymmanagementsystem.entities.administration.dto.RoleDTO;
import com.ubt.gymmanagementsystem.entities.administration.dto.UserDTO;
import com.ubt.gymmanagementsystem.services.administration.RoleService;
import com.ubt.gymmanagementsystem.services.administration.UserService;
import com.ubt.gymmanagementsystem.services.gym.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PersonService personService;

    @GetMapping("/users")
    @PostAuthorize("hasAuthority('READ_USER')")
    public String users(Model model){
        model.addAttribute("users", userService.getAll());
        return "administration/users/users";
    }

    @GetMapping("/addUser")
    @PostAuthorize("hasAuthority('WRITE_USER')")
    public String addUser(Model model){
        model.addAttribute("roles", roleService.getAllEnabled());
        model.addAttribute("persons", personService.getAllEnabledWithoutUsersAssigned());
        return "administration/users/addUser";
    }

    @PostMapping(value = "/addUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_USER')")
    public ModelAndView addUser(@ModelAttribute UserDTO userDTO){

        try {
            boolean created = userService.save(userDTO);

            ModelAndView modelAndView = new ModelAndView("administration/users/users");
            modelAndView.addObject("isCreated", created);
            modelAndView.addObject("users", userService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("administration/users/addUser");
            modelAndView.addObject("userDTO", userDTO);
            modelAndView.addObject("roles", roleService.getAllEnabled());
            modelAndView.addObject("persons", personService.getAllEnabledWithoutUsersAssigned());
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editUser/{id}")
    @PostAuthorize("hasAuthority('WRITE_USER')")
    public String editUser(@PathVariable Long id, Model model){
        UserDTO userDTO = userService.prepareUserDTO(id);
        model.addAttribute("roles", roleService.getAllEnabled());
        model.addAttribute("userDTO", userService.prepareUserDTO(id));
        model.addAttribute("persons", personService.getAllEnabledWithoutUsersAssignedForEdit(userDTO.getPersonId()));
        return "administration/users/editUser";
    }

    @PutMapping(value = "/editUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_USER')")
    public ModelAndView editUser(@ModelAttribute UserDTO userDTO){

        try {
            boolean updated = userService.update(userDTO);

            ModelAndView modelAndView = new ModelAndView("administration/users/users");
            modelAndView.addObject("isUpdated", updated);
            modelAndView.addObject("users", userService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("administration/users/editUser");
            modelAndView.addObject("userDTO", userDTO);
            modelAndView.addObject("roles", roleService.getAllEnabled());
            modelAndView.addObject("persons", personService.getAllEnabledWithoutUsersAssigned());
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/disableUser/{id}")
    @PostAuthorize("hasAuthority('WRITE_USER')")
    public String disableUser(@PathVariable Long id, Model model){

        boolean disabled = userService.disable(id);
        model.addAttribute("isDisabled", disabled);
        model.addAttribute("users", userService.getAll());
        return "administration/users/users";
    }

    @GetMapping("/enableUser/{id}")
    @PostAuthorize("hasAuthority('WRITE_USER')")
    public String enableUser(@PathVariable Long id, Model model){

        boolean enabled = userService.enable(id);
        model.addAttribute("isEnabled", enabled);
        model.addAttribute("users", userService.getAll());
        return "administration/users/users";
    }
}