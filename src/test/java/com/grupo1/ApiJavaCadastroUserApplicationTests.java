package com.grupo1;

import com.grupo1.business.AdminInstructorStrategy;
import com.grupo1.business.EmailService;
import com.grupo1.business.StudentValidationStrategy;
import com.grupo1.business.UserService;
import com.grupo1.infrastructure.entity.Role;
import com.grupo1.infrastructure.entity.User;
import com.grupo1.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private StudentValidationStrategy studentValidationStrategy;
    @Mock
    private AdminInstructorStrategy adminInstructorStrategy;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    private User student;

    @BeforeEach
    void setUp() {

        userService.iniciarStrategies();

        student = User.builder()
                .name("Aluno Teste")
                .email("aluno@teste.com")
                .cpf("12345678901")
                .password("senha123")
                .phone("11999999999")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.STUDENT)
                .registration("2024001")
                .build();
    }

    @Test
    @DisplayName("Deve salvar um novo estudante com sucesso")
    void saveUser_ShouldSaveNewStudentSuccessfully() {
        // Arrange
        when(userRepository.findByEmail(student.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(student.getCpf())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(student.getPassword())).thenReturn("encodedPassword");

        // Act
        userService.saveUser(student);

        // Assert
        verify(passwordEncoder).encode("senha123");
        verify(studentValidationStrategy).validate(student);
        verify(userRepository).save(argThat(user ->
                "encodedPassword".equals(user.getPassword()) &&
                        "aluno@teste.com".equals(user.getEmail())
        ));
    }

    @Test
    @DisplayName("Deve lançar RuntimeException ao tentar salvar com email duplicado")
    void saveUser_ShouldThrowExceptionWhenEmailDuplicated() {
        // Arrange
        when(userRepository.findByEmail(student.getEmail())).thenReturn(Optional.of(student));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.saveUser(student)
        );
        assertEquals("Erro: email already in use by another user.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar RuntimeException ao tentar salvar com CPF duplicado")
    void saveUser_ShouldThrowExceptionWhenCpfDuplicated() {
        // Arrange
        when(userRepository.findByEmail(student.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(student.getCpf())).thenReturn(Optional.of(student));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.saveUser(student)
        );
        assertEquals("Erro: cpf already in use by another user.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException para estudante sem número de registro")
    void saveUser_ShouldThrowExceptionWhenStudentIsMissingRegistration() {
        // Arrange
        User invalidStudent = User.builder()
                .name("Aluno Invalido")
                .email("invalido@teste.com")
                .cpf("99999999999")
                .password("senha123")
                .phone("11999999999")
                .birthDate(LocalDate.of(2000, 1, 1))
                .role(Role.STUDENT)
                .registration(null) // Campo ausente
                .build();

        // Simula o comportamento da StudentValidationStrategy
        doThrow(new IllegalArgumentException("Estudante não tem um número de registro"))
                .when(studentValidationStrategy).validate(invalidStudent);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.saveUser(invalidStudent)
        );
        assertEquals("Estudante não tem um número de registro", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}