package com.ubt.gymmanagementsystem.services.administration;

import java.util.List;

import javax.transaction.Transactional;

import com.ubt.gymmanagementsystem.configurations.exceptions.DatabaseException;
import com.ubt.gymmanagementsystem.entities.administration.Role;
import com.ubt.gymmanagementsystem.entities.administration.User;
import com.ubt.gymmanagementsystem.entities.administration.dto.UserDTO;
import com.ubt.gymmanagementsystem.entities.gym.Person;
import com.ubt.gymmanagementsystem.repositories.administration.UserRepository;
import com.ubt.gymmanagementsystem.services.gym.PersonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("userDetailsService")
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private RoleService roleService;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmailAndEnabled(email, true);

        if (user == null)
            throw new UsernameNotFoundException(email);

        return user;
    }

    public List<User> getAllEnabled(){
        return userRepository.findAllByEnabled(true);
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean save(UserDTO userDTO) throws DatabaseException {

        Person person = personService.getById(userDTO.getPersonId());
        Role role = roleService.getById(userDTO.getRoleId());
        User user = User.builder().username(userDTO.getUsername()).email(userDTO.getEmail()).person(person).role(role).enabled(true).build();
        if(StringUtils.isNotBlank(user.getUsername()) && StringUtils.isNotBlank(user.getEmail())
                && user.getPerson() != null && user.getRole() != null) {
            try {
                userRepository.save(user);
                return true;
            }catch (Exception e) {
                throw new DatabaseException("duplicate");
            }
        }
        else {
            return false;
        }
    }

    public boolean update(UserDTO userDTO) throws DatabaseException {

        Person person = personService.getById(userDTO.getPersonId());
        Role role = roleService.getById(userDTO.getRoleId());
        User user = getById(userDTO.getId());
        User tempUser = User.builder().id(userDTO.getId()).username(userDTO.getUsername()).email(userDTO.getEmail())
                .person(person).role(role).enabled(user.isEnabled()).build();

        if(StringUtils.isNotBlank(tempUser.getUsername()) && StringUtils.isNotBlank(tempUser.getEmail())
                &&  tempUser.getPerson() != null && tempUser.getRole() != null){
            try {
                userRepository.save(tempUser);
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

        User user = getById(id);
        if(userRepository.existsById(user.getId()) && user.isEnabled()){
            user.setEnabled(false);
            userRepository.save(user);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean enable(Long id) {

        User user = getById(id);
        if(userRepository.existsById(user.getId()) && !user.isEnabled()){
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }
        else {
            return false;
        }
    }

    public UserDTO prepareUserDTO(final Long id) {

        User user = getById(id);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPersonId(user.getPerson().getId());
        userDTO.setRoleId(user.getRole().getId());

        return userDTO;
    }
}