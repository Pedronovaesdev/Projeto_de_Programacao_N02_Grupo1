package com.grupo1.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

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
    private LocalDateTime registrationDate;

    @UpdateTimestamp
    @Column(name = "ultimoAcesso")
    private LocalDateTime lastAccess;


    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "matricula", nullable = false)
    private String registration;

    @Column(name = "especialidade")
    private String specialty;

    @Column(name = "registroProf")
    private String teacherRegistration;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Inscricao> inscricoes;

    @OneToMany(mappedBy = "instrutor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Curso> cursosLecionados;
}
