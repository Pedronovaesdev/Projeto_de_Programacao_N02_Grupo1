package com.grupo1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRequestDTO {
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String especialidade;
    private LocalDate dataNascimento;
}
