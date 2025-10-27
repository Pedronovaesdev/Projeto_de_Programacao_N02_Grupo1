package com.grupo1.business;

import com.grupo1.infrastructure.entity.Role;
import com.grupo1.infrastructure.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AdminInstructorStrategy implements UserValidationStrategy{

    @Override
    public void validate(User user){
        if(user.getRegistration() == null){
            throw new RuntimeException("O valor do dado não pode ser nulo");
        }

        if(user.getRole() == Role.ADMIN && user.getTeacherRegistration() != null){
            if(user.getTeacherRegistration().isEmpty()){
                user.setTeacherRegistration(null);
            }
        }

        if(user.getRole() == Role.INSTRUCTOR){
            if(user.getTeacherRegistration() == null ||
            user.getTeacherRegistration().isEmpty()){
                throw new RuntimeException("O instrutor deve ter uma credencial");
            }
        }
    }
}
