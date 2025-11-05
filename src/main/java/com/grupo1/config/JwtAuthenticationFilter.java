package com.grupo1.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService; // Será o seu JpaUserDetailsService

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Verifica se o cabeçalho Authorization existe e começa com "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Se não tiver token, continua para o próximo filtro
            return;
        }

        // 2. Extrai o token (remove o "Bearer ")
        jwt = authHeader.substring(7);

        try {
            // 3. Extrai o email (username) do token
            userEmail = jwtTokenService.getUsernameFromToken(jwt);

            // 4. Verifica se o email existe e se o usuário já não está autenticado
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 5. Carrega os detalhes do usuário do banco
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // 6. Valida o token (compara o usuário do token com o do banco e checa expiração)
                if (jwtTokenService.validateToken(jwt, userDetails)) {

                    // 7. Se o token for válido, cria a autenticação
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // Credenciais (senha) são nulas em autenticação JWT
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 8. Define o usuário como autenticado no Contexto de Segurança do Spring
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Se o token for inválido (expirado, assinatura errada), apenas ignora.
            // O usuário não será autenticado e receberá um 403 (Forbidden) se tentar acessar rota protegida.
        }

        // 9. Continua para o próximo filtro
        filterChain.doFilter(request, response);
    }
}