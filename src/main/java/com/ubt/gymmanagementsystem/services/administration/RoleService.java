package com.ubt.gymmanagementsystem.services.administration;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import com.ubt.gymmanagementsystem.configurations.exceptions.DatabaseException;
import com.ubt.gymmanagementsystem.entities.administration.Permission;
import com.ubt.gymmanagementsystem.entities.administration.Role;
import com.ubt.gymmanagementsystem.entities.administration.daos.RoleDAO;
import com.ubt.gymmanagementsystem.repositories.administration.PermissionRepository;
import com.ubt.gymmanagementsystem.repositories.administration.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Role> getAll(){
        return roleRepository.findAll();
    }

    public Role getById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public boolean save(RoleDAO roleDAO) throws DatabaseException {

        Role role = Role.builder().name(roleDAO.getName()).permissions(preparePermissions(roleDAO)).enabled(true).build();
        if(role.getName() != null && !role.getName().trim().isEmpty()){
            try {
                roleRepository.save(role);
                return true;
            }catch (Exception e) {
                throw new DatabaseException("duplicate");
            }
        }
        else {
            return false;
        }
    }

    public boolean update(RoleDAO roleDAO) throws DatabaseException {

        Role role = getById(roleDAO.getId());
        Role tempRole = Role.builder().id(roleDAO.getId()).name(roleDAO.getName())
                .permissions(preparePermissions(roleDAO)).enabled(role.isEnabled()).build();

        if(tempRole.getName() != null && !tempRole.getName().trim().isEmpty() && roleRepository.existsById(tempRole.getId())){
            try {
                roleRepository.save(tempRole);
                return true;
            }catch (Exception e) {
                throw new DatabaseException("duplicate");
            }
        }
        else {
            return false;
        }
    }

    public boolean disable(Long id) {

        Role role = getById(id);
        if(roleRepository.existsById(role.getId()) && role.isEnabled()){
            role.setEnabled(false);
            roleRepository.save(role);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean enable(Long id) {

        Role role = getById(id);
        if(roleRepository.existsById(role.getId()) && !role.isEnabled()){
            role.setEnabled(true);
            roleRepository.save(role);
            return true;
        }
        else {
            return false;
        }
    }

    public RoleDAO prepareRoleDAO(final Long id) {

        Role role = getById(id);

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