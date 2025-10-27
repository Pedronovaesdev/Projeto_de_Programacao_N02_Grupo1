package com.grupo1.business;

import com.grupo1.infrastructure.entity.User;

public interface UserValidationStrategy {
    void validate(User user);
}
