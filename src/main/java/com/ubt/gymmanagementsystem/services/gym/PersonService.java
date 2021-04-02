package com.ubt.gymmanagementsystem.services.gym;

import com.ubt.gymmanagementsystem.configurations.exceptions.DatabaseException;
import com.ubt.gymmanagementsystem.entities.administration.Permission;
import com.ubt.gymmanagementsystem.entities.gym.Person;
import com.ubt.gymmanagementsystem.entities.gym.daos.PersonDAO;
import com.ubt.gymmanagementsystem.repositories.gym.PersonImageRepository;
import com.ubt.gymmanagementsystem.repositories.gym.PersonRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonImageService personImageService;

    public List<Person> getAll(){
        return personRepository.findAll();
    }

    public Person getById(Long id) {
        return personRepository.findById(id).orElse(null);
    }

    public boolean save(PersonDAO personDAO, MultipartFile image) throws DatabaseException {
        Person person = Person.builder()
                .firstName(personDAO.getFirstName())
                .lastName(personDAO.getLastName())
                .birthDate(getBirthdateFromString(personDAO.getBirthDateString()))
                .gender(personDAO.getGender())
                .personalId(personDAO.getPersonalId())
                .enabled(true).build();

        if(StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())
                && StringUtils.isNotBlank(person.getPersonalId()) && person.getBirthDate() != null
                && (person.getGender() == 'M' || person.getGender() == 'F')) {
            try {
                personRepository.save(person);
                if(image != null) {
                    personImageService.save(image, person);
                }
                return true;
            }catch (Exception e) {
                throw new DatabaseException("duplicate");
            }
        }
        else {
            return false;
        }
    }

    public boolean update(PersonDAO personDAO, MultipartFile image) throws DatabaseException {

        Person person = getById(personDAO.getId());
        Person tempPerson = Person.builder()
                .id(person.getId())
                .firstName(personDAO.getFirstName())
                .lastName(personDAO.getLastName())
                .birthDate(getBirthdateFromString(personDAO.getBirthDateString()))
                .gender(personDAO.getGender())
                .personalId(personDAO.getPersonalId())
                .enabled(true).build();

        if(StringUtils.isNotBlank(tempPerson.getFirstName()) && StringUtils.isNotBlank(tempPerson.getLastName())
                && StringUtils.isNotBlank(tempPerson.getPersonalId()) && tempPerson.getBirthDate() != null
                && (tempPerson.getGender() == 'M' || tempPerson.getGender() == 'F') && personRepository.existsById(tempPerson.getId())) {
            try {
                personRepository.save(tempPerson);
                if(image != null) {
                    personImageService.save(image, person);
                }
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

        Person person = getById(id);
        if(personRepository.existsById(person.getId()) && person.isEnabled()){
            person.setEnabled(false);
            personRepository.save(person);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean enable(Long id) {

        Person person = getById(id);
        if(personRepository.existsById(person.getId()) && !person.isEnabled()){
            person.setEnabled(true);
            personRepository.save(person);
            return true;
        }
        else {
            return false;
        }
    }

    public PersonDAO preparePersonDAO(final Long id) {

        Person person = getById(id);

        return PersonDAO.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .birthDate(person.getBirthDate())
                .gender(person.getGender())
                .personalId(person.getPersonalId())
                .build();
    }

    private Date getBirthdateFromString(String date) {

        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
