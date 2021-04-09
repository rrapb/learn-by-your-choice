package com.ubt.gymmanagementsystem.services.gym;

import com.ubt.gymmanagementsystem.configurations.exceptions.DatabaseException;
import com.ubt.gymmanagementsystem.entities.gym.Category;
import com.ubt.gymmanagementsystem.entities.gym.Person;
import com.ubt.gymmanagementsystem.entities.gym.PlanProgram;
import com.ubt.gymmanagementsystem.entities.gym.dto.PlanProgramDTO;
import com.ubt.gymmanagementsystem.repositories.gym.PlanProgramRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class PlanProgramService {

    @Autowired
    private PlanProgramRepository planProgramRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PersonService personService;

    public List<PlanProgram> getAll(){
        return planProgramRepository.findAll();
    }

    public PlanProgram getById(Long id) {
        return planProgramRepository.findById(id).orElse(null);
    }

    public boolean save(PlanProgramDTO planProgramDTO) throws DatabaseException {

        Person person = personService.getById(planProgramDTO.getPersonId());
        Category category = categoryService.getById(planProgramDTO.getCategoryId());

        PlanProgram planProgram = PlanProgram.builder()
                .day(planProgramDTO.getDay())
                .person(person)
                .category(category)
                .enabled(true).build();

        if(StringUtils.isNotBlank(planProgram.getDay())) {
            try {
                planProgramRepository.save(planProgram);
                return true;
            }catch (Exception e) {
                throw new DatabaseException("duplicate");
            }
        }
        else {
            return false;
        }
    }

    public boolean update(PlanProgramDTO planProgramDTO) throws DatabaseException {

        PlanProgram planProgram = getById(planProgramDTO.getId());
        Person person = personService.getById(planProgramDTO.getPersonId());
        Category category = categoryService.getById(planProgramDTO.getCategoryId());

        PlanProgram tempPlanProgram = PlanProgram.builder()
                .id(planProgram.getId())
                .day(planProgramDTO.getDay())
                .person(person)
                .category(category)
                .enabled(true).build();

        if(StringUtils.isNotBlank(planProgram.getDay()) && planProgramRepository.existsById(tempPlanProgram.getId())) {
            try {
                planProgramRepository.save(tempPlanProgram);
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

        PlanProgram planProgram = getById(id);
        if(planProgram.isEnabled()){
            planProgram.setEnabled(false);
            planProgramRepository.save(planProgram);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean enable(Long id) {

        PlanProgram planProgram = getById(id);
        if(!planProgram.isEnabled()){
            planProgram.setEnabled(true);
            planProgramRepository.save(planProgram);
            return true;
        }
        else {
            return false;
        }
    }

    public byte[] download(Long id) {

        Person person = personService.getById(id);
        List<PlanProgram> planPrograms = planProgramRepository.findAllByPersonAndEnabled(person, true);
        if(!planPrograms.isEmpty()){
            try {
                File file = ResourceUtils.getFile("classpath:planProgramReport/jasper_report.jrxml");
                JasperReport report = JasperCompileManager.compileReport(file.getAbsolutePath());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                HashMap<String, Object> parameters = new HashMap<String, Object>();

                parameters.put("person", person.getFirstName() + " " + person.getLastName());
                parameters.put("date", simpleDateFormat.format(new Date()));

                ArrayList<HashMap<String, String>> list = new ArrayList();
                for (PlanProgram planProgram : planPrograms) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("day", planProgram.getDay());
                    map.put("category", planProgram.getCategory().getName());
                    list.add(map);
                }
                JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(list);
                JasperPrint print = JasperFillManager.fillReport(report, parameters, beanColDataSource);
                return JasperExportManager.exportReportToPdf(print);
            } catch (FileNotFoundException | JRException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public PlanProgramDTO preparePlanProgramDTO(final Long id) {

        PlanProgram planProgram = getById(id);

        return PlanProgramDTO.builder()
                .id(planProgram.getId())
                .day(planProgram.getDay())
                .personId(planProgram.getPerson().getId())
                .categoryId(planProgram.getCategory().getId())
                .build();
    }

}
