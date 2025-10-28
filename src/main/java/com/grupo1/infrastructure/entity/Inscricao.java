package com.grupo1.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_inscricao")
@Entity
public class Inscricao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @CreationTimestamp
    @Column(name = "data_inscricao", nullable = false)
    private LocalDateTime dataInscricao;

    @Column(name = "valor_inscricao", nullable = false)
    private Double valorInscricao;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private User aluno;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;
}