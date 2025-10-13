package com.grupo1.business;

import com.grupo1.infrastructure.entity.User;
import com.grupo1.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user){
        userRepository.saveAndFlush(user);
    }

    public User findByUserEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Not Email")
        );
    }

    public void deleteByEmail(String email){
        userRepository.deleteByEmail(email);
    }

    public List<User> findByAll(){
        return userRepository.findAll();
    }

    public void updateUserById(Integer id, User user){
        User userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("ID NOT FULL"));
        User userUpdate = User.builder()
                .email(user.getEmail() != null ?
                        user.getEmail() : userEntity.getEmail())
                .name(user.getName() != null ?
                        user.getName() : userEntity.getName())
                .id(userEntity.getId())
                .build();
        userRepository.saveAndFlush(userUpdate);
    }
}
