package com.ubt.gymmanagementsystem.configurations.audit;

import java.util.Optional;

import com.ubt.gymmanagementsystem.entities.administration.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            return Optional.of(Optional.of(((User)authentication.getPrincipal()).getUsername()).orElse(authentication.getName()));
        }
        else {
            return Optional.of("SYSTEM");
        }
    }
}