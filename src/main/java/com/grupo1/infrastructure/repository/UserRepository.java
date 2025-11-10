package com.grupo1.infrastructure.repository;

import com.grupo1.infrastructure.entity.Role;
import com.grupo1.infrastructure.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    Optional<User> findByCpf(String cpf);
    List<User> findByRole(Role role);
    @Transactional
    void deleteByEmail(String email);
    @Transactional
    void deleteByCpf(String cpf);
    @Transactional
    void deleteByRole(Role role);
}