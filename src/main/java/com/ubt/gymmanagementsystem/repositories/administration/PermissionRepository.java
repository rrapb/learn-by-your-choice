package com.ubt.gymmanagementsystem.repositories.administration;

import com.ubt.gymmanagementsystem.entities.administration.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findByName(String name);
}
