package com.ubt.lbyc.services.lbyc;

import com.ubt.lbyc.configurations.exceptions.DatabaseException;
import com.ubt.lbyc.entities.lbyc.Course;
import com.ubt.lbyc.entities.lbyc.CourseMaterial;
import com.ubt.lbyc.entities.lbyc.CourseMaterialFile;
import com.ubt.lbyc.entities.lbyc.dto.CourseMaterialDTO;
import com.ubt.lbyc.repositories.lbyc.CourseMaterialRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class CourseMaterialService {

    @Autowired
    private CourseMaterialRepository courseMaterialRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseMaterialFileService courseMaterialFileService;

    @Autowired
    private EntityManager entityManager;

    public List<CourseMaterial> getAllByCourse(Long courseId){

        Course course = courseService.getById(courseId);
        entityManager.clear();
        return courseMaterialRepository.findAllByCourse(course);
    }

    public CourseMaterial getById(Long id) {
        return courseMaterialRepository.findById(id).orElse(null);
    }

    public boolean save(CourseMaterialDTO courseMaterialDTO, MultipartFile file) throws DatabaseException {

        Course course = courseService.getById(courseMaterialDTO.getCourseId());

        CourseMaterial courseMaterial = CourseMaterial.builder()
                .name(courseMaterialDTO.getName())
                .course(course).build();

        if(StringUtils.isNotBlank(courseMaterial.getName()) && course != null) {
            try {
                courseMaterial = courseMaterialRepository.save(courseMaterial);
                CourseMaterialFile courseMaterialFile = null;

                if(file != null) {
                    courseMaterialFile = courseMaterialFileService.save(file, courseMaterial);
                }

                return courseMaterialFile != null;

            }catch (Exception e) {
                throw new DatabaseException("duplicate");
            }
        }
        else {
            return false;
        }
    }

    public boolean update(CourseMaterialDTO courseMaterialDTO) throws DatabaseException {

        CourseMaterial courseMaterial = getById(courseMaterialDTO.getId());
        Course course = courseService.getById(courseMaterialDTO.getCourseId());

        CourseMaterial tempCourseMaterial = CourseMaterial.builder()
                .id(courseMaterial.getId())
                .name(courseMaterialDTO.getName())
                .course(course).build();

        if(StringUtils.isNotBlank(tempCourseMaterial.getName())
                && courseMaterialRepository.existsById(tempCourseMaterial.getId())) {
            try {
                courseMaterialRepository.save(tempCourseMaterial);
                return true;
            }catch (Exception e) {
                throw new DatabaseException("duplicate");
            }
        }
        else {
            return false;
        }
    }

    public boolean delete(Long id) {

        if(courseMaterialRepository.existsById(id)){
            courseMaterialFileService.delete(getById(id));
            courseMaterialRepository.delete(getById(id));
            return true;
        }
        else {
            return false;
        }
    }

    public CourseMaterialDTO prepareCourseMaterialDTO(final Long id) {

        CourseMaterial courseMaterial = getById(id);

        return CourseMaterialDTO.builder()
                .id(courseMaterial.getId())
                .name(courseMaterial.getName())
                .fileId(courseMaterial.getFile().getId())
                .courseId(courseMaterial.getCourse().getId())
                .build();
    }
}
