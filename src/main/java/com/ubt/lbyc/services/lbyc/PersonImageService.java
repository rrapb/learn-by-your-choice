package com.ubt.lbyc.services.lbyc;

import com.ubt.lbyc.entities.lbyc.Person;
import com.ubt.lbyc.entities.lbyc.PersonImage;
import com.ubt.lbyc.repositories.lbyc.PersonImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PersonImageService {

    @Autowired
    private PersonImageRepository personImageRepository;

    public PersonImage save(MultipartFile file, Person person) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if(org.apache.commons.lang3.StringUtils.isNotBlank(fileName)) {
            PersonImage personImage = PersonImage.builder().name(fileName).type(file.getContentType()).data(file.getBytes()).person(person).build();
            PersonImage existingImage = getFile(person);
            if (existingImage != null) {
                personImage.setId(existingImage.getId());
            }
            return personImageRepository.save(personImage);
        }else {
            return null;
        }
    }

    public boolean delete(Person person) {
        PersonImage existingImage = getFile(person);
        personImageRepository.delete(existingImage);
        return true;
    }

    public PersonImage getFile(String id) {
        return personImageRepository.findById(id).orElse(null);
    }

    public PersonImage getFile(Person person) {
        return personImageRepository.findByPerson(person);
    }

}
