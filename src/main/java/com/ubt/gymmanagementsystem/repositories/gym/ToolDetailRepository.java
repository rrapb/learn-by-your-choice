package com.ubt.gymmanagementsystem.repositories.gym;

import com.ubt.gymmanagementsystem.entities.gym.Tool;
import com.ubt.gymmanagementsystem.entities.gym.ToolDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolDetailRepository extends JpaRepository<ToolDetail, Long> {

    List<ToolDetail> findAllByTool(Tool tool);
}
