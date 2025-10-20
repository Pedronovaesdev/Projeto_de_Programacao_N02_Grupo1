package com.grupo1.controller;

import com.grupo1.business.UserService;
import com.grupo1.dto.UserRequestDTO;
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
        newUser.setAccessLevel(userDto.getAccessLevel());
        newUser.setAccountValue(userDto.getAccountValue() != null ? userDto.getAccountValue() : 0.0);
        newUser.setRegistration(userDto.getRegistration());
        newUser.setTeacherRegistration(userDto.getTeacherRegistration());
        userService.saveUser(newUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<User> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.findByUserEmail(email));
    }

    @GetMapping("all")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findByAll());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam String email) {
        userService.deleteByEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Integer id,
                                           @RequestBody UserRequestDTO userDto){
        User newUser = new User();
        newUser.setName(userDto.getName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(userDto.getPassword());
        newUser.setPhone(userDto.getPhone());
        newUser.setSpecialty(userDto.getSpecialty());
        newUser.setBirthDate(userDto.getBirthDate());
        newUser.setAccessLevel(userDto.getAccessLevel());
        newUser.setAccountValue(userDto.getAccountValue());
        newUser.setRegistration(userDto.getRegistration());
        newUser.setTeacherRegistration(userDto.getTeacherRegistration());
        userService.updateUserById(id, newUser);
        return ResponseEntity.ok().build();
    }
}
