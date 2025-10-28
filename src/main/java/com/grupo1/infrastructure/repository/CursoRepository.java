package com.grupo1.infrastructure.repository;

import com.grupo1.infrastructure.entity.Curso;
import com.grupo1.infrastructure.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {

    Optional<Curso> findByNome(String nome);

    List<Curso> findByInstrutor(User instrutor);

    List<Curso> findByInstrutorId(Integer instrutorId);
}