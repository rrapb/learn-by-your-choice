package com.ubt.gymmanagementsystem.repositories.gym;

import com.ubt.gymmanagementsystem.entities.gym.Person;
import com.ubt.gymmanagementsystem.entities.gym.PlanProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanProgramRepository extends JpaRepository<PlanProgram, Long> {

    List<PlanProgram> findAllByPersonAndEnabled(Person person, boolean enabled);
}
