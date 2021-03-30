package com.ubt.gymmanagementsystem.services.administration;

import java.util.List;

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

    public boolean save(Role role) {
        if(role.getName() != null && !role.getName().trim().isEmpty() && roleRepository.findByName(role.getName()) == null){
            roleRepository.save(role);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean update(Role role) {
        if(role.getName() != null && !role.getName().trim().isEmpty() && roleRepository.findByName(role.getName()) != null){
            roleRepository.save(role);
            return true;
        }
        else {
            return false;
        }
    }
}