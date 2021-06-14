package com.ubt.lbyc.repositories.lbyc;

import com.ubt.lbyc.entities.lbyc.Course;
import com.ubt.lbyc.entities.lbyc.CourseMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, Long> {

    List<CourseMaterial> findAllByCourse(@Param("course") Course course);
}