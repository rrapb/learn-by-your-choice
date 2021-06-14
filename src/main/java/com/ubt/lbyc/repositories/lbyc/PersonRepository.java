package com.ubt.lbyc.repositories.lbyc;

import com.ubt.lbyc.entities.lbyc.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findAllByEnabled(boolean enabled);

    @Query(value = "SELECT p FROM Person p LEFT JOIN User u on p = u.person WHERE u.id is null")
    List<Person> findAllWithoutUsers();

    @Query(value = "SELECT p FROM Person p LEFT JOIN User u on p = u.person WHERE p.id = ?1 OR u.id is null")
    List<Person> findAllWithoutUsersForEdit(Long id);
}
