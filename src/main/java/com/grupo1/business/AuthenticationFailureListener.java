package com.grupo1.business;

import com.grupo1.config.SecurityConstants;
import com.grupo1.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private UserRepository userRepository;


    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String email = (String) event.getAuthentication().getPrincipal();


        userRepository.findByEmail(email).ifPresent(user -> {
            int attempts = user.getFailedAttempt() + 1;

            if (attempts >= SecurityConstants.MAX_FAILED_ATTEMPTS) {
                user.setLockTime(LocalDateTime.now());
            }
            user.setFailedAttempt(attempts);
            userRepository.save(user);

        });

    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
