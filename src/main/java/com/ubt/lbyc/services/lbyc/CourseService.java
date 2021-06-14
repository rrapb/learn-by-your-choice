package com.ubt.lbyc.services.lbyc;

import com.ubt.lbyc.configurations.exceptions.DatabaseException;
import com.ubt.lbyc.entities.lbyc.Course;
import com.ubt.lbyc.entities.lbyc.dto.CourseDTO;
import com.ubt.lbyc.repositories.lbyc.CourseRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAll(){
        return courseRepository.findAll();
    }

    public List<Course> getAllEnabled(){
        return courseRepository.findAllByEnabled(true);
    }

    public Course getById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public boolean save(CourseDTO courseDTO) throws DatabaseException {
        Course course = Course.builder()
                .name(courseDTO.getName())
                .description(courseDTO.getDescription())
                .enabled(true).build();

        if(StringUtils.isNotBlank(course.getName())) {
            try {
                courseRepository.save(course);
                return true;
            }catch (Exception e) {
                throw new DatabaseException("duplicate");
            }
        }
        else {
            return false;
        }
    }

    public boolean update(CourseDTO courseDTO) throws DatabaseException {

        Course course = getById(courseDTO.getId());
        Course tempCourse = Course.builder()
                .id(course.getId())
                .name(courseDTO.getName())
                .description(courseDTO.getDescription())
                .enabled(true).build();

        if(StringUtils.isNotBlank(tempCourse.getName())) {
            try {
                courseRepository.save(tempCourse);
                return true;
            }catch (Exception e) {
                throw new DatabaseException("duplicate");
            }
        }
        else {
            return false;
        }
    }

    public boolean disable(Long id) {

        Course course = getById(id);
        if(courseRepository.existsById(course.getId()) && course.isEnabled()){
            course.setEnabled(false);
            courseRepository.save(course);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean enable(Long id) {

        Course course = getById(id);
        if(courseRepository.existsById(course.getId()) && !course.isEnabled()){
            course.setEnabled(true);
            courseRepository.save(course);
            return true;
        }
        else {
            return false;
        }
    }

    public CourseDTO prepareCourseDTO (final Long id) {

        Course course = getById(id);

        return CourseDTO.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .build();
    }
}
