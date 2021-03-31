package com.ubt.gymmanagementsystem.configurations.db;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.ubt.gymmanagementsystem.entities.administration.Permission;
import com.ubt.gymmanagementsystem.entities.gym.Person;
import com.ubt.gymmanagementsystem.entities.administration.Role;
import com.ubt.gymmanagementsystem.entities.administration.User;
import com.ubt.gymmanagementsystem.repositories.administration.PermissionRepository;
import com.ubt.gymmanagementsystem.repositories.gym.PersonRepository;
import com.ubt.gymmanagementsystem.repositories.administration.RoleRepository;
import com.ubt.gymmanagementsystem.repositories.administration.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (userRepository.findByEmail("test@test.com") != null)
            return;
        Permission readRole = createPrivilegeIfNotFound("READ_ROLE");
        Permission writeRole = createPrivilegeIfNotFound("WRITE_ROLE");
        Permission readUser = createPrivilegeIfNotFound("READ_USER");
        Permission writeUser = createPrivilegeIfNotFound("WRITE_USER");
        Permission readPerson = createPrivilegeIfNotFound("READ_PERSON");
        Permission writePerson = createPrivilegeIfNotFound("WRITE_PERSON");

        List<Permission> adminPrivileges = Arrays.asList(readRole, writeRole, readUser, writeUser, readPerson, writePerson);
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        Role userRole = createRoleIfNotFound("ROLE_USER", Arrays.asList(readRole,readUser));

        Person person1 = Person.builder()
                .firstName("Test")
                .lastName("Test")
                .birthDate(new Date())
                .gender('M')
                .personalId("test")
                .enabled(true)
                .build();

        Person person2 = Person.builder()
                .firstName("Test")
                .lastName("Test")
                .birthDate(new Date())
                .gender('M')
                .personalId("test1")
                .enabled(true)
                .build();

        personRepository.save(person1);
        personRepository.save(person2);

        User user = User.builder()
                .password(passwordEncoder.encode("test"))
                .username("testAdmin")
                .email("test@test.com")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .person(person1)
                .role(adminRole)
                .enabled(true)
                .build();

        userRepository.save(user);

        User user2 = User.builder()
                .password(passwordEncoder.encode("test"))
                .username("testUser")
                .email("test2@test.com")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .person(person2)
                .role(userRole)
                .enabled(true)
                .build();

        userRepository.save(user2);
    }

    @Transactional
    public Permission createPrivilegeIfNotFound(String name) {

        Permission permission = permissionRepository.findByName(name);
        if (permission == null) {
            permission = new Permission(name);
            permissionRepository.save(permission);
        }
        return permission;
    }

    @Transactional
    public Role createRoleIfNotFound(String name, Collection<Permission> permissions) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPermissions(permissions);
            role.setEnabled(true);
            roleRepository.save(role);
        }
        return role;
    }
}