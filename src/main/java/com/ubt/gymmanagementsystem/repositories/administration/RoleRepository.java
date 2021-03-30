package com.ubt.gymmanagementsystem.repositories.administration;

import com.ubt.gymmanagementsystem.entities.administration.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
