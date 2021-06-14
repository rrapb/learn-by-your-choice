package com.ubt.lbyc.services.lbyc;

import com.ubt.lbyc.entities.lbyc.CourseMaterial;
import com.ubt.lbyc.entities.lbyc.CourseMaterialFile;
import com.ubt.lbyc.repositories.lbyc.CourseMaterialFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CourseMaterialFileService {

    @Autowired
    private CourseMaterialFileRepository courseMaterialFileRepository;

    public CourseMaterialFile save(MultipartFile file, CourseMaterial courseMaterial) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if(org.apache.commons.lang3.StringUtils.isNotBlank(fileName)) {
            CourseMaterialFile courseMaterialFile = CourseMaterialFile.builder().name(fileName).type(file.getContentType()).data(file.getBytes()).courseMaterial(courseMaterial).build();
            CourseMaterialFile existingCourseMaterialFile = getFile(courseMaterial);
            if (existingCourseMaterialFile != null) {
                courseMaterialFile.setId(existingCourseMaterialFile.getId());
            }
            return courseMaterialFileRepository.save(courseMaterialFile);
        }else {
            return null;
        }
    }

    public boolean delete(CourseMaterial courseMaterial) {
        CourseMaterialFile existingCourseMaterialFile = getFile(courseMaterial);
        courseMaterialFileRepository.delete(existingCourseMaterialFile);
        return true;
    }

    public CourseMaterialFile getFile(String id) {
        return courseMaterialFileRepository.findById(id).orElse(null);
    }

    public CourseMaterialFile getFile(CourseMaterial courseMaterial) {
        return courseMaterialFileRepository.findByCourseMaterial(courseMaterial);
    }

}
