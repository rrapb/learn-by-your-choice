package com.ubt.lbyc.repositories.lbyc;

import com.ubt.lbyc.entities.lbyc.Person;
import com.ubt.lbyc.entities.lbyc.PersonImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonImageRepository extends JpaRepository<PersonImage, String> {

    PersonImage findByPerson(Person person);
}
