package com.grupo1.config;

import com.grupo1.business.JpaUserDetailsService; // Assumindo que você tem essa classe
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecuriyConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JpaUserDetailsService jpaUserDetailsService;

    // Bean do PasswordEncoder (você já tinha)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean do AuthenticationManager (necessário para o AuthenticationController)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Bean principal da cadeia de filtros de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilita o CSRF (desnecessário para APIs stateless)
                .csrf(csrf -> csrf.disable())

            // 2. Define as regras de autorização GLOBAIS
            .authorizeHttpRequests(auth -> auth
                // Permite acesso público a endpoints de autenticação e documentação
                .requestMatchers("/auth/login", "/v3/api-docs/**", "/swagger-ui/**").permitAll()

                        // Permite acesso público ao POST /user (para registrar novos usuários)
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()

                        // Exige autenticação para qualquer outra requisição
                        .anyRequest().authenticated()
                )

                // 3. Define a política de sessão como STATELESS (sem estado)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Informa ao Spring qual UserDetailsService usar
                .userDetailsService(jpaUserDetailsService)

                // 5. Adiciona o seu filtro JWT antes do filtro padrão do Spring
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}