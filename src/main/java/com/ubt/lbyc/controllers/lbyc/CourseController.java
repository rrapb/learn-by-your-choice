package com.ubt.lbyc.controllers.lbyc;

import com.ubt.lbyc.configurations.exceptions.DatabaseException;
import com.ubt.lbyc.entities.lbyc.dto.CourseDTO;
import com.ubt.lbyc.services.lbyc.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/courses")
    @PreAuthorize("hasAuthority('READ_COURSE')")
    public String courses(Model model){
        model.addAttribute("courses", courseService.getAll());
        return "lbyc/courses/courses";
    }

    @GetMapping("/addCourse")
    @PreAuthorize("hasAuthority('WRITE_COURSE')")
    public String addCourse(){
        return "lbyc/courses/addCourse";
    }

    @PostMapping(value = "/addCourse", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_COURSE')")
    public ModelAndView addCourse(@ModelAttribute CourseDTO courseDTO){

        try {
            boolean created = courseService.save(courseDTO);

            ModelAndView modelAndView = new ModelAndView("lbyc/courses/courses");
            modelAndView.addObject("isCreated", created);
            modelAndView.addObject("courses", courseService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("lbyc/courses/addCourse");
            modelAndView.addObject("courseDTO", courseDTO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editCourse/{id}")
    @PreAuthorize("hasAuthority('WRITE_COURSE')")
    public String editCourse(@PathVariable Long id, Model model){
        CourseDTO courseDTO = courseService.prepareCourseDTO(id);
        model.addAttribute("courseDTO", courseDTO);
        return "lbyc/courses/editCourse";
    }

    @PutMapping(value = "/editCourse", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_COURSE')")
    public ModelAndView editCourse(@ModelAttribute CourseDTO courseDTO){

        try {
            boolean updated = courseService.update(courseDTO);

            ModelAndView modelAndView = new ModelAndView("lbyc/courses/courses");
            modelAndView.addObject("isUpdated", updated);
            modelAndView.addObject("courses", courseService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("lbyc/courses/editCourse");
            modelAndView.addObject("courseDTO", courseDTO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/disableCourse/{id}")
    @PreAuthorize("hasAuthority('WRITE_COURSE')")
    public String disableCourse(@PathVariable Long id, Model model){

        boolean disabled = courseService.disable(id);
        model.addAttribute("isDisabled", disabled);
        model.addAttribute("courses", courseService.getAll());
        return "lbyc/courses/courses";
    }

    @GetMapping("/enableCourse/{id}")
    @PreAuthorize("hasAuthority('WRITE_COURSE')")
    public String enableCourse(@PathVariable Long id, Model model){

        boolean enabled = courseService.enable(id);
        model.addAttribute("isEnabled", enabled);
        model.addAttribute("courses", courseService.getAll());
        return "lbyc/courses/courses";
    }
}
