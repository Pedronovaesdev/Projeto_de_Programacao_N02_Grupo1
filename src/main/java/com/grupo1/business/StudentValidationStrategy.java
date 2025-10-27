package com.grupo1.business;

import com.grupo1.infrastructure.entity.User;
import org.springframework.stereotype.Component;

@Component
public class StudentValidationStrategy implements UserValidationStrategy{
    @Override
    public void validate(User user) {
        if (user.getRegistration() == null) {
            throw new IllegalArgumentException("Estudante não tem um número de registro");
        }
    }
}
