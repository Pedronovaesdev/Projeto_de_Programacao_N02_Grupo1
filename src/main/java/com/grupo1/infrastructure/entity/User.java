package com.grupo1.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

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
    private Date dataCadastro;

    @Column(name = "dataNascimento", nullable = false)
    private Date dataNascimento;

    @Column(name = "dataCriacao", nullable = false)
    private Date dataCriacao;

    @Column(name = "ultimoAcesso", nullable = false)
    private Date ultimoAcesso;

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
