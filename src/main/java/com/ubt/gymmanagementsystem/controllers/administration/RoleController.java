package com.ubt.gymmanagementsystem.controllers.administration;

import java.util.ArrayList;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @PostMapping(value = "/addRole", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_ROLE')")
    public String addRole(@RequestBody RoleDAO roleDAO, Model model){

        Role role = Role.builder().name(roleDAO.getName()).permissions(preparePermissions(roleDAO)).build();
        boolean saved = roleService.save(role);
        model.addAttribute("isSaved", saved);
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
}