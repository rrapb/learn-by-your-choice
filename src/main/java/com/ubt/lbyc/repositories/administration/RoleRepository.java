package com.ubt.lbyc.repositories.administration;

import com.ubt.lbyc.entities.administration.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
    List<Role> findAllByEnabled(boolean enabled);
}
