package com.ubt.gymmanagementsystem.repositories.gym;

import com.ubt.gymmanagementsystem.entities.gym.Person;
import com.ubt.gymmanagementsystem.entities.gym.PersonImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonImageRepository extends JpaRepository<PersonImage, String> {

    PersonImage findByPerson(Person person);
}
