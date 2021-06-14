package com.ubt.lbyc.repositories.administration;

import com.ubt.lbyc.entities.administration.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findByEmailAndEnabled(String email, boolean enabled);
    List<User> findAllByEnabled(boolean enabled);
}
