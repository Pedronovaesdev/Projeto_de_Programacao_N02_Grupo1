package com.grupo1.controller;

import com.grupo1.business.UserService;
import com.grupo1.dto.UserRequestDTO;
import com.grupo1.infrastructure.entity.Role;
import com.grupo1.infrastructure.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> saveUser(@RequestBody UserRequestDTO userDto) {
        User newUser = new User();
        newUser.setName(userDto.getName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(userDto.getPassword());
        newUser.setPhone(userDto.getPhone());
        newUser.setSpecialty(userDto.getSpecialty());
        newUser.setBirthDate(userDto.getBirthDate());
        newUser.setRole(userDto.getRole() != null ? userDto.getRole() : Role.STUDENT);
        newUser.setRegistration(userDto.getRegistration());
        newUser.setTeacherRegistration(userDto.getTeacherRegistration());
        
        userService.saveUser(newUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<User> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.findByUserEmail(email));
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findByAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Integer id,
                                           @RequestBody UserRequestDTO userDto) {
        User updateUser = new User();
        updateUser.setName(userDto.getName());
        updateUser.setEmail(userDto.getEmail());
        updateUser.setPassword(userDto.getPassword());
        updateUser.setPhone(userDto.getPhone());
        updateUser.setSpecialty(userDto.getSpecialty());
        updateUser.setBirthDate(userDto.getBirthDate());
        updateUser.setRole(userDto.getRole());
        updateUser.setRegistration(userDto.getRegistration());
        updateUser.setTeacherRegistration(userDto.getTeacherRegistration());
        
        userService.updateUserById(id, updateUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUserByEmail(@RequestParam String email) {
        userService.deleteByEmail(email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable Integer id, @RequestParam Role role) {
        User user = new User();
        user.setRole(role);
        userService.updateUserById(id, user);
        return ResponseEntity.ok().build();
    }
}