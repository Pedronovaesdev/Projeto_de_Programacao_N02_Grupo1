package com.grupo1.business;

import com.grupo1.infrastructure.entity.Role;
import com.grupo1.infrastructure.entity.User;
import com.grupo1.infrastructure.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final StudentValidationStrategy studentValidationStrategy;
    private final AdminInstructorStrategy adminInstructorStrategy;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private Map<Role, UserValidationStrategy> validationStrategies;

    @PostConstruct
    public void iniciarStrategies(){
        validationStrategies = Map.of(
            Role.STUDENT, studentValidationStrategy,
            Role.INSTRUCTOR, adminInstructorStrategy,
            Role.ADMIN, adminInstructorStrategy
        );
    }

    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o email: " + email));

        String token = UUID.randomUUID().toString();
        LocalDateTime expiryTime = LocalDateTime.now().plusHours(1); 

        user.setPasswordResetToken(token);
        user.setTokenExpiryTime(expiryTime);

        userRepository.save(user);

        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    public void completePasswordReset(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new RuntimeException("Token de redefinição inválido ou expirado."));

        if (user.getTokenExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token de redefinição expirado.");
        }

        if (newPassword == null || newPassword.length() < 8) {
             throw new IllegalArgumentException("A nova senha deve ter pelo menos 8 caracteres.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        
        user.setPasswordResetToken(null);
        user.setTokenExpiryTime(null);

        userRepository.save(user);
    }

    public void saveUser(User user) {
        Optional<User> existingEmail = userRepository.findByEmail(user.getEmail());
        if (existingEmail.isPresent()) {
            if (!existingEmail.get().getId().equals(user.getId())) {
                throw new RuntimeException("Erro: email already in use by another user.");
            }
        }

        Optional<User> existingCpf = userRepository.findByCpf(user.getCpf());
        if(existingCpf.isPresent()) {
            if (!existingCpf.get().getId().equals(user.getId())) {
                throw new RuntimeException("Erro: cpf already in use by another user.");
            }
        }

        validateUserByRole(user);

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("A senha não pode ser nula ou vazia.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    public void updateUserById(Integer id, User user) {
        User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        if (user.getEmail() != null && !user.getEmail().equals(userEntity.getEmail())) {
            Optional<User> emailExists = userRepository.findByEmail(user.getEmail());
            if (emailExists.isPresent()) {
                throw new RuntimeException("Email already in use by another user");
            }
            userEntity.setEmail(user.getEmail());
        }

        if (user.getCpf() != null && !user.getCpf().equals(userEntity.getCpf())) {
            Optional<User> cpfExists = userRepository.findByCpf(user.getCpf());
            if (cpfExists.isPresent()) {
                throw new RuntimeException("CPF already in use by another user");
            }
            userEntity.setCpf(user.getCpf());
        }

        String newPassword = user.getPassword();
        if (newPassword != null && !newPassword.isBlank()) {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
        }
        if (user.getName() != null) userEntity.setName(user.getName());
        if (user.getPhone() != null) userEntity.setPhone(user.getPhone());
        if (user.getSpecialty() != null) userEntity.setSpecialty(user.getSpecialty());
        if (user.getBirthDate() != null) userEntity.setBirthDate(user.getBirthDate());
        if (user.getRole() != null) userEntity.setRole(user.getRole());
        if (user.getRegistration() != null) userEntity.setRegistration(user.getRegistration());
        if (user.getTeacherRegistration() != null) userEntity.setTeacherRegistration(user.getTeacherRegistration());

        validateUserByRole(userEntity);
        userRepository.save(userEntity);
    }

    public User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public List<User> findByRole(Role role) {

        if (role == null) {
            throw new IllegalArgumentException("O tipo de usuário (Role) não pode ser nulo.");
        }


        List<User> user = userRepository.findByRole(role);


        return user != null ? user : List.of();
    }

    public List<User> findByAll() {
        return userRepository.findAll();
    }

    public void deleteByEmail(String email) {
        User user = findByUserEmail(email);
        userRepository.delete(user);
    }

    public void deleteByRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("O tipo de usuário role não pode ser nulo.");
        }

        List<User> usuarios = userRepository.findByRole(role);

        if (usuarios.isEmpty()) {
            throw new RuntimeException("Nenhum usuário encontrado com o tipo: " + role);
        }

        userRepository.delete(usuarios.get(0));
    }

    public void deleteById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        userRepository.delete(user);
    }

    private void validateUserByRole(User user) {
        if(user.getRole() == null){
            throw new IllegalArgumentException("Usuário deve ter um papel definido");
        }
        UserValidationStrategy strategy = validationStrategies.get(user.getRole());
        if(strategy == null){
            throw new IllegalArgumentException("Papel não suportado: " + user.getRole());
        }
        strategy.validate(user);
    }
}