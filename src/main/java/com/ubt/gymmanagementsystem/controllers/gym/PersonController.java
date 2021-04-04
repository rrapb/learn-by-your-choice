package com.ubt.gymmanagementsystem.controllers.gym;

import com.ubt.gymmanagementsystem.configurations.exceptions.DatabaseException;
import com.ubt.gymmanagementsystem.entities.gym.PersonImage;
import com.ubt.gymmanagementsystem.entities.gym.dto.PersonDTO;
import com.ubt.gymmanagementsystem.services.gym.PersonImageService;
import com.ubt.gymmanagementsystem.services.gym.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PersonController {

    @Autowired
    private PersonService personService;
    @Autowired
    private PersonImageService personImageService;

    @GetMapping("/persons")
    @PostAuthorize("hasAuthority('READ_PERSON')")
    public String persons(Model model){
        model.addAttribute("persons", personService.getAll());
        return "gym/persons/persons";
    }

    @GetMapping("/addPerson")
    @PostAuthorize("hasAuthority('WRITE_PERSON')")
    public String addPerson(){
        return "gym/persons/addPerson";
    }

    @PostMapping(value = "/addPerson", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_PERSON')")
    public ModelAndView addPerson(@ModelAttribute PersonDTO personDTO, @RequestParam("image") MultipartFile image){

        try {
            boolean created = personService.save(personDTO, image);

            ModelAndView modelAndView = new ModelAndView("gym/persons/persons");
            modelAndView.addObject("isCreated", created);
            modelAndView.addObject("persons", personService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/persons/addPerson");
            modelAndView.addObject("personDTO", personDTO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editPerson/{id}")
    @PostAuthorize("hasAuthority('WRITE_PERSON')")
    public String editPerson(@PathVariable Long id, Model model){
        PersonDTO personDTO = personService.preparePersonDTO(id);
        model.addAttribute("personDTO", personDTO);
        model.addAttribute("gender", personDTO.getGender() == 'M');
        return "gym/persons/editPerson";
    }

    @PutMapping(value = "/editPerson", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_PERSON')")
    public ModelAndView editPerson(@ModelAttribute PersonDTO personDTO, @RequestParam("image") MultipartFile image){

        try {
            boolean updated = personService.update(personDTO, image);

            ModelAndView modelAndView = new ModelAndView("gym/persons/persons");
            modelAndView.addObject("isUpdated", updated);
            modelAndView.addObject("persons", personService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/persons/editPerson");
            modelAndView.addObject("personDTO", personDTO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/disablePerson/{id}")
    @PostAuthorize("hasAuthority('WRITE_PERSON')")
    public String disablePerson(@PathVariable Long id, Model model){

        boolean disabled = personService.disable(id);
        model.addAttribute("isDisabled", disabled);
        model.addAttribute("persons", personService.getAll());
        return "gym/persons/persons";
    }

    @GetMapping("/enablePerson/{id}")
    @PostAuthorize("hasAuthority('WRITE_PERSON')")
    public String enablePerson(@PathVariable Long id, Model model){

        boolean enabled = personService.enable(id);
        model.addAttribute("isEnabled", enabled);
        model.addAttribute("persons", personService.getAll());
        return "gym/persons/persons";
    }

    @GetMapping("/person/images/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        PersonImage personImage = personImageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + personImage.getName() + "\"")
                .body(personImage.getData());
    }
}
