package com.ubt.gymmanagementsystem.services.administration;

import java.util.List;

import javax.transaction.Transactional;

import com.ubt.gymmanagementsystem.entities.administration.User;
import com.ubt.gymmanagementsystem.repositories.administration.RoleRepository;
import com.ubt.gymmanagementsystem.repositories.administration.UserRepository;
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
    private RoleRepository roleRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null)
            throw new UsernameNotFoundException(email);

        return user;
    }
}