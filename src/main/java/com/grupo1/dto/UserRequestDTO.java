package com.grupo1.dto;

import com.grupo1.infrastructure.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String name;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\d{3}\\d{3}\\d{2}", message = "CPF deve conter 11 dígitos")
    private String cpf;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, max = 100, message = "Senha deve ter entre 8 e 100 caracteres")
    private String password;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\d{2}\\d{5}\\d{4}", message = "Telefone deve conter 11 dígitos")
    private String phone;

    private String specialty;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate birthDate;
    
    @NotNull(message = "Role é obrigatória")
    private Role role;
    
    private String registration;
    private String teacherRegistration;
}