package com.ubt.gymmanagementsystem.services.gym;

import com.ubt.gymmanagementsystem.configurations.exceptions.DatabaseException;
import com.ubt.gymmanagementsystem.entities.gym.Category;
import com.ubt.gymmanagementsystem.entities.gym.Tool;
import com.ubt.gymmanagementsystem.entities.gym.dto.ToolDTO;
import com.ubt.gymmanagementsystem.repositories.gym.ToolRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolService {

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private CategoryService categoryService;

    public List<Tool> getAll(){
        return toolRepository.findAll();
    }

    public List<Tool> getAllEnabled(){
        return toolRepository.findAllByEnabled(true);
    }

    public Tool getById(Long id) {
        return toolRepository.findById(id).orElse(null);
    }

    public boolean save(ToolDTO toolDTO) throws DatabaseException {

        Category category = categoryService.getById(toolDTO.getCategoryId());

        Tool tool = Tool.builder()
                .name(toolDTO.getName())
                .description(toolDTO.getDescription())
                .category(category)
                .enabled(true).build();

        if(StringUtils.isNotBlank(tool.getName()) && StringUtils.isNotBlank(tool.getDescription())) {
            try {
                toolRepository.save(tool);
                return true;
            }catch (Exception e) {
                throw new DatabaseException("duplicate");
            }
        }
        else {
            return false;
        }
    }

    public boolean update(ToolDTO toolDTO) throws DatabaseException {

        Tool tool = getById(toolDTO.getId());
        Category category = categoryService.getById(toolDTO.getCategoryId());

        Tool tempTool = Tool.builder()
                .id(tool.getId())
                .name(toolDTO.getName())
                .description(toolDTO.getDescription())
                .category(category)
                .enabled(true).build();

        if(StringUtils.isNotBlank(tempTool.getName()) && StringUtils.isNotBlank(tempTool.getDescription())
                && toolRepository.existsById(tempTool.getId())) {
            try {
                toolRepository.save(tempTool);
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

        Tool tool = getById(id);
        if(tool.isEnabled()){
            tool.setEnabled(false);
            toolRepository.save(tool);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean enable(Long id) {

        Tool tool = getById(id);
        if(!tool.isEnabled()){
            tool.setEnabled(false);
            toolRepository.save(tool);
            return true;
        }
        else {
            return false;
        }
    }

    public ToolDTO prepareToolDTO(final Long id) {

        Tool tool = getById(id);

        return ToolDTO.builder()
                .id(tool.getId())
                .name(tool.getName())
                .description(tool.getDescription())
                .categoryId(tool.getCategory().getId())
                .build();
    }

}
