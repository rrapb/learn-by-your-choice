package com.ubt.lbyc.services.administration;

import java.util.ArrayList;
import java.util.List;

import com.ubt.lbyc.configurations.exceptions.DatabaseException;
import com.ubt.lbyc.entities.administration.Permission;
import com.ubt.lbyc.entities.administration.Role;
import com.ubt.lbyc.entities.administration.User;
import com.ubt.lbyc.entities.administration.dto.RoleDTO;
import com.ubt.lbyc.repositories.administration.PermissionRepository;
import com.ubt.lbyc.repositories.administration.RoleRepository;
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

    public List<Role> getAllEnabled(){
        return roleRepository.findAllByEnabled(true);
    }

    public Role getById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public boolean save(RoleDTO roleDTO) throws DatabaseException {

        Role role = Role.builder().name(roleDTO.getName()).permissions(preparePermissions(roleDTO)).enabled(true).build();
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

    public boolean update(RoleDTO roleDTO) throws DatabaseException {

        Role role = getById(roleDTO.getId());
        Role tempRole = Role.builder().id(roleDTO.getId()).name(roleDTO.getName())
                .permissions(preparePermissions(roleDTO)).enabled(role.isEnabled()).build();

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
        if(roleRepository.existsById(role.getId()) && role.isEnabled()
                && (role.getUsers().isEmpty() || usersAreDisabled(role.getUsers()))){
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

    public RoleDTO prepareRoleDTO(final Long id) {

        Role role = getById(id);

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        for(Permission permission: role.getPermissions()){
            if (permission.getName().equals("READ_ROLE"))
                roleDTO.setViewRoles(true);
            else if (permission.getName().equals("WRITE_ROLE"))
                roleDTO.setAddRoles(true);
            else if (permission.getName().equals("READ_USER"))
                roleDTO.setViewUsers(true);
            else if (permission.getName().equals("WRITE_USER"))
                roleDTO.setAddUsers(true);
            else if (permission.getName().equals("READ_PERSON"))
                roleDTO.setViewPersons(true);
            else if (permission.getName().equals("WRITE_PERSON"))
                roleDTO.setAddPersons(true);
            else if (permission.getName().equals("READ_CATEGORY"))
                roleDTO.setViewCategories(true);
            else if (permission.getName().equals("WRITE_CATEGORY"))
                roleDTO.setAddCategories(true);
            else if (permission.getName().equals("READ_COURSE"))
                roleDTO.setViewCourses(true);
            else if (permission.getName().equals("WRITE_COURSE"))
                roleDTO.setAddCourses(true);
            else if (permission.getName().equals("READ_MATERIAL"))
                roleDTO.setViewMaterials(true);
            else if (permission.getName().equals("WRITE_MATERIAL"))
                roleDTO.setAddMaterials(true);
        }

        return roleDTO;
    }

    private ArrayList<Permission> preparePermissions(final RoleDTO roleDTO) {

        ArrayList<Permission> permissions = new ArrayList<>();

        if(roleDTO.isViewRoles())
            permissions.add(permissionRepository.findByName("READ_ROLE"));
        if (roleDTO.isAddRoles())
            permissions.add(permissionRepository.findByName("WRITE_ROLE"));
        if (roleDTO.isViewUsers())
            permissions.add(permissionRepository.findByName("READ_USER"));
        if (roleDTO.isAddUsers())
            permissions.add(permissionRepository.findByName("WRITE_USER"));
        if (roleDTO.isViewPersons())
            permissions.add(permissionRepository.findByName("READ_PERSON"));
        if (roleDTO.isAddPersons())
            permissions.add(permissionRepository.findByName("WRITE_PERSON"));
        if (roleDTO.isViewCategories())
            permissions.add(permissionRepository.findByName("READ_CATEGORY"));
        if (roleDTO.isAddCategories())
            permissions.add(permissionRepository.findByName("WRITE_CATEGORY"));
        if (roleDTO.isViewCourses())
            permissions.add(permissionRepository.findByName("READ_COURSE"));
        if (roleDTO.isAddCourses())
            permissions.add(permissionRepository.findByName("WRITE_COURSE"));
        if (roleDTO.isViewMaterials())
            permissions.add(permissionRepository.findByName("READ_MATERIAL"));
        if (roleDTO.isAddMaterials())
            permissions.add(permissionRepository.findByName("WRITE_MATERIAL"));

        return permissions;
    }

    private boolean usersAreDisabled(List<User> users) {
        return users.stream().noneMatch(User::isEnabled);
    }
}