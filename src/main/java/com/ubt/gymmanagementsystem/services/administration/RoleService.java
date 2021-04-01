package com.ubt.gymmanagementsystem.services.administration;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import com.ubt.gymmanagementsystem.configurations.exceptions.DatabaseException;
import com.ubt.gymmanagementsystem.entities.administration.Role;
import com.ubt.gymmanagementsystem.repositories.administration.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAll(){
        return roleRepository.findAll();
    }

    public Role getById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public boolean save(Role role) throws DatabaseException {
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

    public boolean update(Role role) throws DatabaseException {
        if(role.getName() != null && !role.getName().trim().isEmpty() && roleRepository.existsById(role.getId())){
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

    public boolean disable(Role role) {
        if(roleRepository.existsById(role.getId()) && role.isEnabled()){
            role.setEnabled(false);
            roleRepository.save(role);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean enable(Role role) {
        if(roleRepository.existsById(role.getId()) && !role.isEnabled()){
            role.setEnabled(true);
            roleRepository.save(role);
            return true;
        }
        else {
            return false;
        }
    }
}