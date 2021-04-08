package com.ubt.gymmanagementsystem.controllers.gym;

import com.ubt.gymmanagementsystem.configurations.exceptions.DatabaseException;
import com.ubt.gymmanagementsystem.entities.gym.dto.ToolDetailDTO;
import com.ubt.gymmanagementsystem.services.gym.ToolDetailService;
import com.ubt.gymmanagementsystem.services.gym.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ToolDetailController {

    @Autowired
    private ToolDetailService toolDetailService;

    @Autowired
    private ToolService toolService;

    @GetMapping("/toolDetails/{id}")
    @PostAuthorize("hasAuthority('READ_TOOL')")
    public String toolDetails(@PathVariable Long id, Model model){
        model.addAttribute("toolDetails", toolDetailService.getAllByTool(id));
        return "gym/toolDetails/toolDetails";
    }

    @GetMapping("/addToolDetail")
    @PostAuthorize("hasAuthority('WRITE_TOOL')")
    public String addToolDetail(Model model){

        model.addAttribute("tools", toolService.getAllEnabled());
        return "gym/toolDetails/addToolDetail";
    }

    @PostMapping(value = "/addToolDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_TOOL')")
    public ModelAndView addToolDetail(@ModelAttribute ToolDetailDTO toolDetailDTO){

        try {
            boolean created = toolDetailService.save(toolDetailDTO);

            ModelAndView modelAndView = new ModelAndView("gym/toolDetails/toolDetails");
            modelAndView.addObject("isCreated", created);
            modelAndView.addObject("toolDetails", toolDetailService.getAllByTool(toolDetailDTO.getToolId()));
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/toolDetails/addToolDetail");
            modelAndView.addObject("toolDetailDTO", toolDetailDTO);
            modelAndView.addObject("tools", toolService.getAllEnabled());
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editToolDetail/{id}")
    @PostAuthorize("hasAuthority('WRITE_TOOL')")
    public String editToolDetail(@PathVariable Long id, Model model){
        ToolDetailDTO toolDetailDTO = toolDetailService.prepareToolDetailDTO(id);
        model.addAttribute("toolDetailDTO", toolDetailDTO);
        model.addAttribute("tools", toolService.getAllEnabled());
        return "gym/toolDetails/editToolDetail";
    }

    @PutMapping(value = "/editToolDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_TOOL')")
    public ModelAndView editToolDetail(@ModelAttribute ToolDetailDTO toolDetailDTO){

        try {
            boolean updated = toolDetailService.update(toolDetailDTO);

            ModelAndView modelAndView = new ModelAndView("gym/toolDetails/toolDetails");
            modelAndView.addObject("isUpdated", updated);
            modelAndView.addObject("toolDetails", toolDetailService.getAllByTool(toolDetailDTO.getToolId()));
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/toolDetails/editToolDetail");
            modelAndView.addObject("toolDetailDTO", toolDetailDTO);
            modelAndView.addObject("tools", toolService.getAllEnabled());
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/deleteToolDetail/{id}")
    @PostAuthorize("hasAuthority('WRITE_TOOL')")
    public String deleteToolDetail(@PathVariable Long id, Model model){

        Long toolId = toolDetailService.getById(id).getTool().getId();
        boolean deleted = toolDetailService.delete(id);
        model.addAttribute("isDeleted", deleted);
        model.addAttribute("toolDetails", toolDetailService.getAllByTool(toolId));
        return "gym/toolDetails/toolDetails";
    }
}
