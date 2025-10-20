package com.grupo1.business;

import com.grupo1.infrastructure.entity.Role;
import com.grupo1.infrastructure.entity.User;
import com.grupo1.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void saveUser(User user) {
        Optional<User> existingEmail = userRepository.findByEmail(user.getEmail());

        if (existingEmail.isPresent()) {
            if (!existingEmail.get().getId().equals(user.getId())) {
                throw new RuntimeException("Erro: O email informado já está em uso por outro usuário.");
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

        User userUpdate = User.builder()
                .id(userEntity.getId())
                .email(user.getEmail() != null ? user.getEmail() : userEntity.getEmail())
                .name(user.getName() != null ? user.getName() : userEntity.getName())
                .phone(user.getPhone() != null ? user.getPhone() : userEntity.getPhone())
                .password(user.getPassword() != null ? user.getPassword() : userEntity.getPassword())
                .specialty(user.getSpecialty() != null ? user.getSpecialty() : userEntity.getSpecialty())
                .birthDate(user.getBirthDate() != null ? user.getBirthDate() : userEntity.getBirthDate())
                .role(user.getRole() != null ? user.getRole() : userEntity.getRole())
                .accountValue(user.getAccountValue() != null ? user.getAccountValue() : userEntity.getAccountValue())
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
        if (user.getRole() == Role.STUDENT) {
            if (user.getSpecialty() != null && !user.getSpecialty().isEmpty()) {
                throw new RuntimeException("Aluno (STUDENT) não pode ter especialidade");
            }
            if (user.getTeacherRegistration() != null && !user.getTeacherRegistration().isEmpty()) {
                throw new RuntimeException("Aluno (STUDENT) não pode ter registro profissional");
            }
            user.setSpecialty(null);
            user.setTeacherRegistration(null);
        } else if (user.getRole() == Role.INSTRUCTOR || user.getRole() == Role.ADMIN) {
            if (user.getSpecialty() == null || user.getSpecialty().isEmpty()) {
                throw new RuntimeException("Instrutor/Admin deve ter especialidade");
            }
        }
    }
}