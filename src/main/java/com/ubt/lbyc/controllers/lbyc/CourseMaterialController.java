package com.ubt.lbyc.controllers.lbyc;

import com.ubt.lbyc.configurations.exceptions.DatabaseException;
import com.ubt.lbyc.entities.lbyc.Course;
import com.ubt.lbyc.entities.lbyc.CourseMaterialFile;
import com.ubt.lbyc.entities.lbyc.dto.CourseMaterialDTO;
import com.ubt.lbyc.services.lbyc.CourseMaterialFileService;
import com.ubt.lbyc.services.lbyc.CourseMaterialService;
import com.ubt.lbyc.services.lbyc.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class CourseMaterialController {

    @Autowired
    private CourseMaterialService courseMaterialService;

    @Autowired
    private CourseMaterialFileService courseMaterialFileService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/courseMaterials/{id}")
    @PreAuthorize("hasAuthority('READ_MATERIAL')")
    public String courseMaterials(@PathVariable Long id, Model model){
        model.addAttribute("courseMaterials", courseMaterialService.getAllByCourse(id));
        model.addAttribute("course", courseService.getById(id));
        return "lbyc/courseMaterials/courseMaterials";
    }

    @GetMapping("/addCourseMaterial")
    @PreAuthorize("hasAuthority('WRITE_MATERIAL')")
    public String addCourseMaterial(@RequestParam("course") String courseId, Model model, HttpServletResponse response) throws IOException {
        Course course = courseService.getById(Long.parseLong(courseId));
        if(!course.isEnabled())
            response.sendRedirect("/403");
        model.addAttribute("courses", courseService.getAllEnabled());
        model.addAttribute("selectedCourse", course);
        return "lbyc/courseMaterials/addCourseMaterial";
    }

    @PostMapping(value = "/addCourseMaterial", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_MATERIAL')")
    public ModelAndView addCourseMaterial(@ModelAttribute CourseMaterialDTO courseMaterialDTO, MultipartFile file){

        try {
            boolean created = courseMaterialService.save(courseMaterialDTO, file);

            ModelAndView modelAndView = new ModelAndView("lbyc/courseMaterials/courseMaterials");
            modelAndView.addObject("isCreated", created);
            modelAndView.addObject("course", courseService.getById(courseMaterialDTO.getCourseId()));
            modelAndView.addObject("courseMaterials", courseMaterialService.getAllByCourse(courseMaterialDTO.getCourseId()));
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("lbyc/courseMaterials/addCourseMaterial");
            modelAndView.addObject("courseMaterialDTO", courseMaterialDTO);
            modelAndView.addObject("courses", courseService.getAllEnabled());
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editCourseMaterial/{id}")
    @PreAuthorize("hasAuthority('WRITE_MATERIAL')")
    public String editCourseMaterial(@PathVariable Long id, Model model){
        CourseMaterialDTO courseMaterialDTO = courseMaterialService.prepareCourseMaterialDTO(id);
        model.addAttribute("courseMaterialDTO", courseMaterialDTO);
        model.addAttribute("courses", courseService.getAllEnabled());
        return "lbyc/courseMaterials/editCourseMaterial";
    }

    @PutMapping(value = "/editCourseMaterial", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_MATERIAL')")
    public ModelAndView editCourseMaterial(@ModelAttribute CourseMaterialDTO courseMaterialDTO){

        try {
            boolean updated = courseMaterialService.update(courseMaterialDTO);

            ModelAndView modelAndView = new ModelAndView("lbyc/courseMaterials/courseMaterials");
            modelAndView.addObject("course", courseService.getById(courseMaterialDTO.getCourseId()));
            modelAndView.addObject("isUpdated", updated);
            modelAndView.addObject("courseMaterials", courseMaterialService.getAllByCourse(courseMaterialDTO.getCourseId()));
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("lbyc/courseMaterials/editCourseMaterial");
            modelAndView.addObject("courseMaterialDTO", courseMaterialDTO);
            modelAndView.addObject("courses", courseService.getAllEnabled());
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/deleteCourseMaterial/{id}")
    @PreAuthorize("hasAuthority('WRITE_MATERIAL')")
    public String deleteCourseMaterial(@PathVariable Long id, Model model){

        Long courseId = courseMaterialService.getById(id).getCourse().getId();
        boolean deleted = courseMaterialService.delete(id);
        model.addAttribute("isDeleted", deleted);
        model.addAttribute("course", courseService.getById(courseId));
        model.addAttribute("courseMaterials", courseMaterialService.getAllByCourse(courseId));
        return "lbyc/courseMaterials/courseMaterials";
    }

    @GetMapping("/courseMaterials/file/{id}")
    @PreAuthorize("hasAuthority('READ_MATERIAL')")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        CourseMaterialFile courseMaterialFile = courseMaterialFileService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + courseMaterialFile.getName() + "\"")
                .body(courseMaterialFile.getData());
    }
}
