package com.grupo1.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_curso")
@Entity
public class Curso implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Column(name = "valor_curso", nullable = false)
    private Double valorCurso;

    @Column(name = "duracao")
    private String duracao; 

    @ManyToOne
    @JoinColumn(name = "instrutor_id", nullable = false)
    private User instrutor;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Inscricao> inscricoes = new HashSet<>();
}