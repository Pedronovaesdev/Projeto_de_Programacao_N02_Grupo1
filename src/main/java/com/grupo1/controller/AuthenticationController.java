package com.grupo1.controller;

import com.grupo1.config.JwtTokenService;
import com.grupo1.dto.LoginRequestDTO;
import com.grupo1.dto.LoginResponseDTO;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        // 1. Autentica o usuário usando o email e a senha
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Se a autenticação for bem-sucedida, obtém os UserDetails
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Gera o token JWT
        String token = jwtTokenService.generateToken(userDetails);

        // 4. Retorna o token para o cliente
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
