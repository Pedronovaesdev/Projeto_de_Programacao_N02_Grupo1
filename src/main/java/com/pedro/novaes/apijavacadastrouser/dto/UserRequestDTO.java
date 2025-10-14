package com.pedro.novaes.apijavacadastrouser.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRequestDTO {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String specialty;
    private LocalDate birthDate;
}
