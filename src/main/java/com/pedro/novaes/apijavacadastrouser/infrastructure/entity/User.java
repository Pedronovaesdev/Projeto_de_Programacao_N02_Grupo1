package com.pedro.novaes.apijavacadastrouser.infrastructure.entity;

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
    private String senha;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(name = "dataCadastro", nullable = false)
    private LocalDate dataCadastro;

    @Column(name = "dataNascimento", nullable = false)
    private LocalDate dataNascimento;

    @CreationTimestamp
    @Column(name = "dataCriacao", nullable = false)
    private LocalDate dataCriacao;

    @UpdateTimestamp
    @Column(name = "ultimoAcesso")
    private LocalDateTime ultimoAcesso;

    @Column(name = "ValorConta", nullable = false)
    private Double valorConta;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "matricula", nullable = false)
    private String matricula;

    @Column(name = "especialidade", nullable = false)
    private String especialidade;

    @Column(name = "registroProf", nullable = false)
    private String registroProf;

    @Column(name = "nivelAcesso", nullable = false)
    private String nivelAcesso;


}
