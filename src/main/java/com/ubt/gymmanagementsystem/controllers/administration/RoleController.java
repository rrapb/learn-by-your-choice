package com.ubt.gymmanagementsystem.controllers.administration;

import java.util.ArrayList;

import com.ubt.gymmanagementsystem.configurations.exceptions.DatabaseException;
import com.ubt.gymmanagementsystem.entities.administration.Permission;
import com.ubt.gymmanagementsystem.entities.administration.Role;
import com.ubt.gymmanagementsystem.repositories.administration.PermissionRepository;
import com.ubt.gymmanagementsystem.services.administration.RoleService;
import com.ubt.gymmanagementsystem.entities.administration.daos.RoleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionRepository permissionRepository;

    @GetMapping("/roles")
    @PostAuthorize("hasAuthority('READ_ROLE')")
    public String roles(Model model){
        model.addAttribute("roles", roleService.getAll());
        return "administration/roles/roles";
    }

    @GetMapping("/addRole")
    @PostAuthorize("hasAuthority('WRITE_ROLE')")
    public String addRole(Model model){
        return "administration/roles/addRole";
    }

    @PostMapping(value = "/addRole", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_ROLE')")
    public ModelAndView addRole(@ModelAttribute RoleDAO roleDAO){
        try {
            Role role = Role.builder().name(roleDAO.getName()).permissions(preparePermissions(roleDAO)).enabled(true).build();
            boolean created = roleService.save(role);

            ModelAndView modelAndView = new ModelAndView("administration/roles/roles");
            modelAndView.addObject("isCreated", created);
            modelAndView.addObject("roles", roleService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("administration/roles/addRole");
            modelAndView.addObject("roleDAO", roleDAO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editRole/{id}")
    @PostAuthorize("hasAuthority('WRITE_ROLE')")
    public String editRole(@PathVariable Long id, Model model){

        Role role = roleService.getById(id);
        model.addAttribute("roleDAO", prepareRoleDAO(role));
        return "administration/roles/editRole";
    }

    @PutMapping(value = "/editRole", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_ROLE')")
    public ModelAndView editRole(@ModelAttribute RoleDAO roleDAO){
        try {
            Role role = roleService.getById(roleDAO.getId());
            Role tempRole = Role.builder().id(roleDAO.getId()).name(roleDAO.getName())
                    .permissions(preparePermissions(roleDAO)).enabled(role.isEnabled()).build();
            boolean updated = roleService.update(tempRole);

            ModelAndView modelAndView = new ModelAndView("administration/roles/roles");
            modelAndView.addObject("isUpdated", updated);
            modelAndView.addObject("roles", roleService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("administration/roles/editRole");
            modelAndView.addObject("roleDAO", roleDAO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/disableRole/{id}")
    @PostAuthorize("hasAuthority('WRITE_ROLE')")
    public String disableRole(@PathVariable Long id, Model model){

        Role role = roleService.getById(id);
        boolean disabled = roleService.disable(role);
        model.addAttribute("isDisabled", disabled);
        model.addAttribute("roles", roleService.getAll());
        return "administration/roles/roles";
    }

    @GetMapping("/enableRole/{id}")
    @PostAuthorize("hasAuthority('WRITE_ROLE')")
    public String enableRole(@PathVariable Long id, Model model){

        Role role = roleService.getById(id);
        boolean enabled = roleService.enable(role);
        model.addAttribute("isEnabled", enabled);
        model.addAttribute("roles", roleService.getAll());
        return "administration/roles/roles";
    }

    private ArrayList<Permission> preparePermissions(final RoleDAO roleDAO) {

        ArrayList<Permission> permissions = new ArrayList<>();

        if(roleDAO.isViewRoles())
            permissions.add(permissionRepository.findByName("READ_ROLE"));
        if (roleDAO.isAddRoles())
            permissions.add(permissionRepository.findByName("WRITE_ROLE"));
        if (roleDAO.isViewUsers())
            permissions.add(permissionRepository.findByName("READ_USER"));
        if (roleDAO.isAddUsers())
            permissions.add(permissionRepository.findByName("WRITE_USER"));
        if (roleDAO.isViewPersons())
            permissions.add(permissionRepository.findByName("READ_PERSON"));
        if (roleDAO.isAddPersons())
            permissions.add(permissionRepository.findByName("WRITE_PERSON"));

        return permissions;
    }

    private RoleDAO prepareRoleDAO(final Role role) {

        RoleDAO roleDAO = new RoleDAO();
        roleDAO.setId(role.getId());
        roleDAO.setName(role.getName());
        for(Permission permission: role.getPermissions()){
            if (permission.getName().equals("READ_ROLE"))
                roleDAO.setViewRoles(true);
            else if (permission.getName().equals("WRITE_ROLE"))
                roleDAO.setAddRoles(true);
            else if (permission.getName().equals("READ_USER"))
                roleDAO.setViewUsers(true);
            else if (permission.getName().equals("WRITE_USER"))
                roleDAO.setAddUsers(true);
            else if (permission.getName().equals("READ_PERSON"))
                roleDAO.setViewPersons(true);
            else if (permission.getName().equals("WRITE_PERSON"))
                roleDAO.setAddPersons(true);
        }

        return roleDAO;
    }
}