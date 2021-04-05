package com.ubt.gymmanagementsystem.repositories.gym;

import com.ubt.gymmanagementsystem.entities.gym.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByEnabled(boolean enabled);

}
