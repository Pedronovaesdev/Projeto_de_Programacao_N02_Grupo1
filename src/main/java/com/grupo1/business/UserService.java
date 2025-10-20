package com.grupo1.business;

import com.grupo1.infrastructure.entity.User;
import com.grupo1.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {

        Optional<User> Email = userRepository.findByEmail(user.getEmail());

        if (Email.isPresent()) {
            if (!Email.get().getId().equals(user.getId())) {
                throw new RuntimeException("Erro: O email informado já está em uso por outro usuário.");
            }
        }

        userRepository.save(user);
    }

    public void updateUserById(Integer id, User user){
        User userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("ID NOT FULL"));
        User userUpdate = User.builder()
                .email(user.getEmail() != null ?
                        user.getEmail() : userEntity.getEmail())
                .name(user.getName() != null ?
                        user.getName() : userEntity.getName())
                .phone(user.getPhone() != null ?
                        user.getPhone() : userEntity.getPhone())
                .password(user.getPassword() != null ?
                        user.getPassword() : userEntity.getPassword())
                .specialty(user.getSpecialty() != null ?
                        user.getSpecialty() : userEntity.getSpecialty())
                .birthDate(user.getBirthDate() != null ?
                        user.getBirthDate() : userEntity.getBirthDate())
                .id(userEntity.getId())
                .build();
        userRepository.saveAndFlush(userUpdate);
    }
}
