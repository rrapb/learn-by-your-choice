package com.ubt.lbyc.repositories.lbyc;

import com.ubt.lbyc.entities.lbyc.CourseMaterial;
import com.ubt.lbyc.entities.lbyc.CourseMaterialFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseMaterialFileRepository extends JpaRepository<CourseMaterialFile, String> {

    CourseMaterialFile findByCourseMaterial(CourseMaterial courseMaterial);

    CourseMaterialFile save(CourseMaterial courseMaterial);
}
