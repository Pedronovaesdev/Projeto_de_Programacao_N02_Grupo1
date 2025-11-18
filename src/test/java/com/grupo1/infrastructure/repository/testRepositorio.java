package com.grupo1.infrastructure.repository;

import com.grupo1.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.grupo1.infrastructure.entity.Role;
import com.grupo1.infrastructure.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class testRepositorio {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User createUser(String email, String cpf, Role role) {

        User user = User.builder()
                .name("Nome Teste")
                .email(email)
                .cpf(cpf)
                .password("senhaCriptografada")
                .phone("11999999999")
                .birthDate(LocalDate.of(1990, 1, 1))
                .role(role)
                .registration(role == Role.STUDENT ? "REG123" : "0000000") // Valor default
                .teacherRegistration(role != Role.STUDENT ? "PROF456" : null)
                .build();
        return entityManager.persistFlushFind(user);
    }

    @Test
    @DisplayName("Deve encontrar usuário por email")
    void findByEmail_ShouldReturnUserWhenEmailExists() {

        User savedUser = createUser("teste@email.com", "11122233344", Role.STUDENT);


        Optional<User> found = userRepository.findByEmail("teste@email.com");


        assertTrue(found.isPresent());
        assertEquals(savedUser.getId(), found.get().getId());
        assertEquals("teste@email.com", found.get().getEmail());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando email não existe")
    void findByEmail_ShouldReturnEmptyOptionalWhenEmailDoesNotExist() {

        Optional<User> found = userRepository.findByEmail("naoexiste@email.com");


        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Deve encontrar usuário por CPF")
    void findByCpf_ShouldReturnUserWhenCpfExists() {

        User savedUser = createUser("outro@email.com", "99988877766", Role.INSTRUCTOR);


        Optional<User> found = userRepository.findByCpf("99988877766");


        assertTrue(found.isPresent());
        assertEquals(savedUser.getId(), found.get().getId());
        assertEquals("99988877766", found.get().getCpf());
    }

    @Test
    @DisplayName("Deve listar todos os usuários por uma Role específica")
    void findByRole_ShouldListUsersByRole() {

        createUser("admin@email.com", "00011122233", Role.ADMIN);
        createUser("aluno1@email.com", "11122233344", Role.STUDENT);
        createUser("aluno2@email.com", "22233344455", Role.STUDENT);


        List<User> students = userRepository.findByRole(Role.STUDENT);
        List<User> admins = userRepository.findByRole(Role.ADMIN);
        List<User> instructors = userRepository.findByRole(Role.INSTRUCTOR);


        assertEquals(2, students.size());
        assertEquals(1, admins.size());
        assertEquals(0, instructors.size());
    }
}