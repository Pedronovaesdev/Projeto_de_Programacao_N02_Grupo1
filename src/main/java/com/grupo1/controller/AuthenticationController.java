package com.grupo1.controller;

import com.grupo1.business.UserService;
import com.grupo1.config.JwtTokenService;
import com.grupo1.dto.LoginRequestDTO;
import com.grupo1.dto.LoginResponseDTO;
import com.grupo1.dto.PasswordResetCompletionDTO;
import com.grupo1.dto.PasswordResetRequestDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtTokenService.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody PasswordResetRequestDTO request) {
        try {
            userService.initiatePasswordReset(request.getEmail());
            return ResponseEntity.ok("E-mail de redefinição de senha enviado.");
        } catch (Exception e) {
            return ResponseEntity.ok("Se o e-mail estiver cadastrado, um link será enviado.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetCompletionDTO request) {
        try {
            userService.completePasswordReset(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok("Senha redefinida com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
