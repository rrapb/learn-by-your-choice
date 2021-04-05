package com.ubt.gymmanagementsystem.repositories.gym;

import com.ubt.gymmanagementsystem.entities.gym.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {

    List<Tool> findAllByEnabled(boolean enabled);
}
