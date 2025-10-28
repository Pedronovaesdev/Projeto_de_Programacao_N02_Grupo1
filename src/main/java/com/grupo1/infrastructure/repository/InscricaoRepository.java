package com.grupo1.infrastructure.repository;

import com.grupo1.infrastructure.entity.Curso;
import com.grupo1.infrastructure.entity.Inscricao;
import com.grupo1.infrastructure.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Integer> {

    List<Inscricao> findByAluno(User aluno);

    List<Inscricao> findByAlunoId(Integer alunoId);

    List<Inscricao> findByCurso(Curso curso);

    List<Inscricao> findByCursoId(Integer cursoId);

    Optional<Inscricao> findByAlunoIdAndCursoId(Integer alunoId, Integer cursoId);
}