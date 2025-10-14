package com.grupo1.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_user")
@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id     
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "senha", nullable = false)
    private String password ;

    @Column(name = "telefone", nullable = false)
    private String phone;

    @Column(name = "dataNascimento", nullable = false)
    private LocalDate birthDate;

    @CreationTimestamp
    @Column(name = "dataCriacao", nullable = false)
    private LocalDate registrionDate;

    @UpdateTimestamp
    @Column(name = "ultimoAcesso")
    private LocalDateTime lastAccess;

    @Column(name = "ValorConta", nullable = false)
    private Double accountValue;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "matricula", nullable = false)
    private String registration;

    @Column(name = "especialidade", nullable = false)
    private String specialty;

    @Column(name = "registroProf", nullable = false)
    private String teacherRegistration;

    @Column(name = "nivelAcesso", nullable = false)
    private String accessLevel;


}
