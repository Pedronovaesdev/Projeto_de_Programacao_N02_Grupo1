package com.pedro.novaes.apijavacadastrouser.infrastructure.repository;

import com.pedro.novaes.apijavacadastrouser.infrastructure.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    @Transactional
    void deleteByEmail(String email);
}
