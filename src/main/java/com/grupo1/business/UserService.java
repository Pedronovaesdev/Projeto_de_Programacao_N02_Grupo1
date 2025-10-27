package com.grupo1.business;

import com.grupo1.infrastructure.entity.Role;
import com.grupo1.infrastructure.entity.User;
import com.grupo1.infrastructure.repository.UserRepository;
import com.grupo1.business.AdminInstructorStrategy;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final StudentValidationStrategy studentValidationStrategy;
    private final AdminInstructorStrategy adminInstructorStrategy;

    private Map<Role, UserValidationStrategy> validationStrategies;

    @PostConstruct
    private void iniciarStrategies(){
        validationStrategies = Map.of(
            Role.STUDENT, studentValidationStrategy,
            Role.INSTRUCTOR, adminInstructorStrategy,
            Role.ADMIN, adminInstructorStrategy
        );
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
        }

        if (user.getCpf() != null && !user.getCpf().equals(userEntity.getCpf())) {
            Optional<User> cpfExists = userRepository.findByCpf(user.getCpf());
            if (cpfExists.isPresent()) {
                throw new RuntimeException("CPF already in use by another user");
            }
        }

        User userUpdate = User.builder()
                .id(userEntity.getId())
                .email(user.getEmail() != null ? user.getEmail() : userEntity.getEmail())
                .name(user.getName() != null ? user.getName() : userEntity.getName())
                .phone(user.getPhone() != null ? user.getPhone() : userEntity.getPhone())
                .password(user.getPassword() != null ? user.getPassword() : userEntity.getPassword())
                .specialty(user.getSpecialty() != null ? user.getSpecialty() : userEntity.getSpecialty())
                .birthDate(user.getBirthDate() != null ? user.getBirthDate() : userEntity.getBirthDate())
                .role(user.getRole() != null ? user.getRole() : userEntity.getRole())
                .registration(user.getRegistration() != null ? user.getRegistration() : userEntity.getRegistration())
                .teacherRegistration(user.getTeacherRegistration() != null ? 
                        user.getTeacherRegistration() : userEntity.getTeacherRegistration())
                .build();
        
        validateUserByRole(userUpdate);
        
        userRepository.saveAndFlush(userUpdate);
    }

    public User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public List<User> findByAll() {
        return userRepository.findAll();
    }

    public void deleteByEmail(String email) {
        User user = findByUserEmail(email);
        userRepository.delete(user);
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