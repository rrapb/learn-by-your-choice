package com.ubt.lbyc.controllers.administration;

import com.ubt.lbyc.configurations.exceptions.DatabaseException;
import com.ubt.lbyc.entities.administration.dto.RoleDTO;
import com.ubt.lbyc.services.administration.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('READ_ROLE')")
    public String roles(Model model){
        model.addAttribute("roles", roleService.getAll());
        return "administration/roles/roles";
    }

    @GetMapping("/addRole")
    @PreAuthorize("hasAuthority('WRITE_ROLE')")
    public String addRole(){
        return "administration/roles/addRole";
    }

    @PostMapping(value = "/addRole", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_ROLE')")
    public ModelAndView addRole(@ModelAttribute RoleDTO roleDTO){

        try {
            boolean created = roleService.save(roleDTO);

            ModelAndView modelAndView = new ModelAndView("administration/roles/roles");
            modelAndView.addObject("isCreated", created);
            modelAndView.addObject("roles", roleService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("administration/roles/addRole");
            modelAndView.addObject("roleDTO", roleDTO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editRole/{id}")
    @PreAuthorize("hasAuthority('WRITE_ROLE')")
    public String editRole(@PathVariable Long id, Model model){

        model.addAttribute("roleDTO", roleService.prepareRoleDTO(id));
        return "administration/roles/editRole";
    }

    @PutMapping(value = "/editRole", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_ROLE')")
    public ModelAndView editRole(@ModelAttribute RoleDTO roleDTO){

        try {
            boolean updated = roleService.update(roleDTO);

            ModelAndView modelAndView = new ModelAndView("administration/roles/roles");
            modelAndView.addObject("isUpdated", updated);
            modelAndView.addObject("roles", roleService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("administration/roles/editRole");
            modelAndView.addObject("roleDTO", roleDTO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/disableRole/{id}")
    @PreAuthorize("hasAuthority('WRITE_ROLE')")
    public String disableRole(@PathVariable Long id, Model model){

        boolean disabled = roleService.disable(id);
        model.addAttribute("isDisabled", disabled);
        model.addAttribute("roles", roleService.getAll());
        return "administration/roles/roles";
    }

    @GetMapping("/enableRole/{id}")
    @PreAuthorize("hasAuthority('WRITE_ROLE')")
    public String enableRole(@PathVariable Long id, Model model){

        boolean enabled = roleService.enable(id);
        model.addAttribute("isEnabled", enabled);
        model.addAttribute("roles", roleService.getAll());
        return "administration/roles/roles";
    }
}