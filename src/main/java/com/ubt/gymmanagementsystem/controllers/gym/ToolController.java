package com.ubt.gymmanagementsystem.controllers.gym;

import com.ubt.gymmanagementsystem.configurations.exceptions.DatabaseException;
import com.ubt.gymmanagementsystem.entities.gym.dto.ToolDTO;
import com.ubt.gymmanagementsystem.services.gym.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ToolController {

    @Autowired
    private ToolService toolService;

    @GetMapping("/tools")
    @PostAuthorize("hasAuthority('READ_TOOL')")
    public String tools(Model model){
        model.addAttribute("tools", toolService.getAll());
        return "gym/tools/tools";
    }

    @GetMapping("/addTool")
    @PostAuthorize("hasAuthority('WRITE_TOOL')")
    public String addTool(){
        return "gym/tools/addTool";
    }

    @PostMapping(value = "/addTool", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_TOOL')")
    public ModelAndView addTool(@ModelAttribute ToolDTO toolDTO){

        try {
            boolean created = toolService.save(toolDTO);

            ModelAndView modelAndView = new ModelAndView("gym/tools/tools");
            modelAndView.addObject("isCreated", created);
            modelAndView.addObject("tools", toolService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/tools/addTool");
            modelAndView.addObject("toolDTO", toolDTO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editTool/{id}")
    @PostAuthorize("hasAuthority('WRITE_TOOL')")
    public String editTool(@PathVariable Long id, Model model){
        ToolDTO toolDTO = toolService.prepareToolDTO(id);
        model.addAttribute("toolDTO", toolDTO);
        return "gym/tools/tools";
    }

    @PutMapping(value = "/editTool", produces = MediaType.APPLICATION_JSON_VALUE)
    @PostAuthorize("hasAuthority('WRITE_TOOL')")
    public ModelAndView editTool(@ModelAttribute ToolDTO toolDTO){

        try {
            boolean updated = toolService.update(toolDTO);

            ModelAndView modelAndView = new ModelAndView("gym/tools/tools");
            modelAndView.addObject("isUpdated", updated);
            modelAndView.addObject("tools", toolService.getAll());
            return modelAndView;
        }
        catch (DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/tools/editTool");
            modelAndView.addObject("toolDTO", toolDTO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/disableTool/{id}")
    @PostAuthorize("hasAuthority('WRITE_TOOL')")
    public String disableTool(@PathVariable Long id, Model model){

        boolean disabled = toolService.disable(id);
        model.addAttribute("isDisabled", disabled);
        model.addAttribute("tools", toolService.getAll());
        return "gym/tools/tools";
    }

    @GetMapping("/enableTool/{id}")
    @PostAuthorize("hasAuthority('WRITE_TOOL')")
    public String enableTool(@PathVariable Long id, Model model){

        boolean enabled = toolService.enable(id);
        model.addAttribute("isEnabled", enabled);
        model.addAttribute("tools", toolService.getAll());
        return "gym/tools/tools";
    }
}
