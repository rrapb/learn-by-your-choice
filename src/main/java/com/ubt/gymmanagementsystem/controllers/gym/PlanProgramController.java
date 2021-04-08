package com.ubt.gymmanagementsystem.controllers.gym;

import com.ubt.gymmanagementsystem.configurations.exceptions.DatabaseException;
import com.ubt.gymmanagementsystem.entities.gym.Person;
import com.ubt.gymmanagementsystem.entities.gym.dto.PlanProgramDTO;
import com.ubt.gymmanagementsystem.services.gym.CategoryService;
import com.ubt.gymmanagementsystem.services.gym.PersonService;
import com.ubt.gymmanagementsystem.services.gym.PlanProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PlanProgramController {

    @Autowired
    private PlanProgramService planProgramService;

    @Autowired
    private PersonService personService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/planPrograms")
    @PostAuthorize("hasAuthority('READ_PLAN_PROGRAM')")
    public String planPrograms(Model model){
        model.addAttribute("planPrograms", planProgramService.getAll());
        model.addAttribute("persons", personService.getAllEnabled());
        return "gym/planPrograms/planPrograms";
    }

    @GetMapping("/addPlanProgram")
    @PostAuthorize("hasAuthority('WRITE_PLAN_PROGRAM')")
    public String addPlanProgram(Model model){

        model.addAttribute("persons", personService.getAllEnabled());
        model.addAttribute("categories", categoryService.getAllEnabled());
        return "gym/planPrograms/addPlanProgram";
    }

    @PostMapping(value = "/addPlanProgram", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_PLAN_PROGRAM')")
    public ModelAndView addPlanProgram(@ModelAttribute PlanProgramDTO planProgramDTO){

        try {
            boolean created = planProgramService.save(planProgramDTO);

            ModelAndView modelAndView = new ModelAndView("gym/planPrograms/planPrograms");
            modelAndView.addObject("persons", personService.getAllEnabled());
            modelAndView.addObject("isCreated", created);
            modelAndView.addObject("planPrograms", planProgramService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/planPrograms/addPlanProgram");
            modelAndView.addObject("planProgramDTO", planProgramDTO);
            modelAndView.addObject("persons", personService.getAllEnabled());
            modelAndView.addObject("categories", categoryService.getAllEnabled());
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editPlanProgram/{id}")
    @PostAuthorize("hasAuthority('WRITE_PLAN_PROGRAM')")
    public String editPlanProgram(@PathVariable Long id, Model model){
        PlanProgramDTO planProgramDTO = planProgramService.preparePlanProgramDTO(id);
        model.addAttribute("planProgramDTO", planProgramDTO);
        model.addAttribute("persons", personService.getAllEnabled());
        model.addAttribute("categories", categoryService.getAllEnabled());
        return "gym/planPrograms/editPlanProgram";
    }

    @PutMapping(value = "/editPlanProgram", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_PLAN_PROGRAM')")
    public ModelAndView editPlanProgram(@ModelAttribute PlanProgramDTO planProgramDTO){

        try {
            boolean updated = planProgramService.update(planProgramDTO);

            ModelAndView modelAndView = new ModelAndView("gym/planPrograms/planPrograms");
            modelAndView.addObject("isUpdated", updated);
            modelAndView.addObject("persons", personService.getAllEnabled());
            modelAndView.addObject("planPrograms", planProgramService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/planPrograms/editPlanProgram");
            modelAndView.addObject("planProgramDTO", planProgramDTO);
            modelAndView.addObject("persons", personService.getAllEnabled());
            modelAndView.addObject("categories", categoryService.getAllEnabled());
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/disablePlanProgram/{id}")
    @PostAuthorize("hasAuthority('WRITE_PLAN_PROGRAM')")
    public String disablePlanProgram(@PathVariable Long id, Model model){

        boolean disabled = planProgramService.disable(id);
        model.addAttribute("isDisabled", disabled);
        model.addAttribute("persons", personService.getAllEnabled());
        model.addAttribute("planPrograms", planProgramService.getAll());
        return "gym/planPrograms/planPrograms";
    }

    @GetMapping("/enablePlanProgram/{id}")
    @PostAuthorize("hasAuthority('WRITE_PLAN_PROGRAM')")
    public String enablePlanProgram(@PathVariable Long id, Model model){

        boolean enabled = planProgramService.enable(id);
        model.addAttribute("isEnabled", enabled);
        model.addAttribute("persons", personService.getAllEnabled());
        model.addAttribute("planPrograms", planProgramService.getAll());
        return "gym/planPrograms/planPrograms";
    }

    @GetMapping("/downloadPlanProgram")
    @PostAuthorize("hasAuthority('READ_PLAN_PROGRAM')")
    public ResponseEntity<byte[]> downloadPlanProgram(@ModelAttribute PlanProgramDTO planProgramDTO){

        Person person = personService.getById(planProgramDTO.getPersonId());
        byte[] pdfFile = planProgramService.download(planProgramDTO.getPersonId());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + person.getFirstName() + "_" + person.getLastName() + ".pdf"+ "\"")
                .body(pdfFile);
    }
}
