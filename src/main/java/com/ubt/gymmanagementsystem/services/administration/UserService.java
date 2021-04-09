package com.ubt.gymmanagementsystem.services.administration;

import java.util.Base64;
import java.util.List;

import javax.transaction.Transactional;

import com.ubt.gymmanagementsystem.configurations.exceptions.DatabaseException;
import com.ubt.gymmanagementsystem.entities.administration.EmailNotification;
import com.ubt.gymmanagementsystem.entities.administration.Role;
import com.ubt.gymmanagementsystem.entities.administration.User;
import com.ubt.gymmanagementsystem.entities.administration.UserRegistrationEmailModel;
import com.ubt.gymmanagementsystem.entities.administration.dto.UserDTO;
import com.ubt.gymmanagementsystem.entities.gym.Person;
import com.ubt.gymmanagementsystem.repositories.administration.UserRepository;
import com.ubt.gymmanagementsystem.services.gym.PersonService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.username}")
    private String senderEmail;

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

    public UserDetails loadUserByEmailAndPassword(String email, String password) throws UsernameNotFoundException {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User user = userRepository.findByEmail(email);

        if (user == null)
            throw new UsernameNotFoundException(email);

        boolean isPasswordMatch = passwordEncoder.matches(password, user.getPassword());
        return isPasswordMatch ? user : null;
    }

    public boolean updatePassword(String email, String password) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(password);
        User user = userRepository.findByEmail(email);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        return true;
    }

    public boolean activateUser(String email) {

        User user = userRepository.findByEmail(email);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }

    public List<User> getAllEnabled(){
        return userRepository.findAllByEnabled(true);
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean save(UserDTO userDTO) throws DatabaseException {

        String randomPassword = RandomStringUtils.randomAlphanumeric(8);

        String encodedPassword = Base64.getEncoder().encodeToString(randomPassword.getBytes());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(randomPassword);

        Person person = personService.getById(userDTO.getPersonId());
        Role role = roleService.getById(userDTO.getRoleId());
        User user = User.builder().username(userDTO.getUsername()).email(userDTO.getEmail()).person(person).role(role)
                .password(encryptedPassword).enabled(false).build();
        if(StringUtils.isNotBlank(user.getUsername()) && StringUtils.isNotBlank(user.getEmail())
                && user.getPerson() != null && user.getRole() != null) {
            try {
                userRepository.save(user);

                final UserRegistrationEmailModel userRegistrationEmailModel = UserRegistrationEmailModel
                        .builder()
                        .firstName(user.getPerson().getFirstName())
                        .lastName(user.getPerson().getLastName())
                        .url("http://localhost:8080/welcome/"+encodedPassword)
                        .build();

                final EmailNotification emailNotification = EmailNotification
                        .builder()
                        .sender(senderEmail)
                        .receiver(user.getEmail())
                        .subject("Register")
                        .content(userRegistrationEmailModel)
                        .build();

                emailService.sendEmail(emailNotification);

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

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .personId(user.getPerson().getId())
                .roleId(user.getRole().getId())
                .build();
    }
}