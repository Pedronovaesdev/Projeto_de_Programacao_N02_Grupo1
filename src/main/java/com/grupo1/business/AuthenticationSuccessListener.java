package com.grupo1.business;

import com.grupo1.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener
        implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String email = event.getAuthentication().getName();

        userRepository.findByEmail(email).ifPresent(user -> {
            if (user.getFailedAttempt() > 0) {
                user.setFailedAttempt(0);
            }
            if (user.getLockTime() != null) {
                user.setLockTime(null);
            }
            userRepository.save(user);
        });
    }
}