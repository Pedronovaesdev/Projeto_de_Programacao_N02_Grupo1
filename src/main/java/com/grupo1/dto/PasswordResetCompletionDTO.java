package com.grupo1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetCompletionDTO {
    private String token;
    private String newPassword;
}
