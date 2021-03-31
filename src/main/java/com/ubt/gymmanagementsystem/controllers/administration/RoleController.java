package com.ubt.gymmanagementsystem.controllers.administration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public @ResponseBody Map addRole(@ModelAttribute RoleDAO roleDAO){
        Role role = Role.builder().name(roleDAO.getName()).permissions(preparePermissions(roleDAO)).build();
        boolean saved = roleService.save(role);
        Map map = new HashMap();
        map.put("isSaved", saved);
        return map;
    }

    @GetMapping("/editRole/{id}")
    @PostAuthorize("hasAuthority('WRITE_ROLE')")
    public String editRole(@PathVariable Long id, Model model){

        Role role = roleService.getById(id);
        model.addAttribute("roleDAO", prepareRoleDAO(role));
        return "administration/roles/editRole";
    }

    @PostMapping(value = "/editRole", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_ROLE')")
    public @ResponseBody Map editRole(@ModelAttribute RoleDAO roleDAO){
        Role role = Role.builder().id(roleDAO.getId()).name(roleDAO.getName()).permissions(preparePermissions(roleDAO)).build();
        boolean saved = roleService.update(role);
        Map map = new HashMap();
        map.put("isSaved", saved);
        return map;
    }

    @GetMapping("/deleteRole/{id}")
    @PostAuthorize("hasAuthority('WRITE_ROLE')")
    public String deleteRole(@PathVariable Long id, Model model){

        Role role = roleService.getById(id);
        boolean deleted = roleService.delete(role);
        model.addAttribute("deleted", deleted);
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