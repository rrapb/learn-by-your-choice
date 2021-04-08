package com.ubt.gymmanagementsystem.controllers.gym;

import com.ubt.gymmanagementsystem.configurations.exceptions.DatabaseException;
import com.ubt.gymmanagementsystem.entities.gym.dto.CategoryDTO;
import com.ubt.gymmanagementsystem.services.gym.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    @PostAuthorize("hasAuthority('READ_CATEGORY')")
    public String categories(Model model){
        model.addAttribute("categories", categoryService.getAll());
        return "gym/categories/categories";
    }

    @GetMapping("/addCategory")
    @PostAuthorize("hasAuthority('WRITE_CATEGORY')")
    public String addCategory(){
        return "gym/categories/addCategory";
    }

    @PostMapping(value = "/addCategory", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_CATEGORY')")
    public ModelAndView addCategory(@ModelAttribute CategoryDTO categoryDTO){

        try {
            boolean created = categoryService.save(categoryDTO);

            ModelAndView modelAndView = new ModelAndView("gym/categories/categories");
            modelAndView.addObject("isCreated", created);
            modelAndView.addObject("categories", categoryService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/categories/addCategory");
            modelAndView.addObject("categoryDTO", categoryDTO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editCategory/{id}")
    @PostAuthorize("hasAuthority('WRITE_CATEGORY')")
    public String editCategory(@PathVariable Long id, Model model){
        CategoryDTO categoryDTO = categoryService.prepareCategoryDTO(id);
        model.addAttribute("categoryDTO", categoryDTO);
        return "gym/categories/editCategory";
    }

    @PutMapping(value = "/editCategory", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_CATEGORY')")
    public ModelAndView editCategory(@ModelAttribute CategoryDTO categoryDTO){

        try {
            boolean updated = categoryService.update(categoryDTO);

            ModelAndView modelAndView = new ModelAndView("gym/categories/categories");
            modelAndView.addObject("isUpdated", updated);
            modelAndView.addObject("categories", categoryService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/categories/editCategory");
            modelAndView.addObject("categoryDTO", categoryDTO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/disableCategory/{id}")
    @PostAuthorize("hasAuthority('WRITE_CATEGORY')")
    public String disableCategory(@PathVariable Long id, Model model){

        boolean disabled = categoryService.disable(id);
        model.addAttribute("isDisabled", disabled);
        model.addAttribute("categories", categoryService.getAll());
        return "gym/categories/categories";
    }

    @GetMapping("/enableCategory/{id}")
    @PostAuthorize("hasAuthority('WRITE_CATEGORY')")
    public String enableCategory(@PathVariable Long id, Model model){

        boolean enabled = categoryService.enable(id);
        model.addAttribute("isEnabled", enabled);
        model.addAttribute("categories", categoryService.getAll());
        return "gym/categories/categories";
    }
}
