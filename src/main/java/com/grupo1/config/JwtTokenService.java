package com.grupo1.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenService {
    private static final String SECRET_KEY = "ta+6LTJo8ybEdHUaCQQ0NQDOiApUhG9OQp8cm5UqE8MbJiF4HsaifQRpVz9secT7\n" +
            "O048ycKNwCdgvycSl/O30w==";

    // Tempo de expiração do token (ex: 10 horas)
    private static final long JWT_TOKEN_VALIDITY = 10 * 60 * 60 * 1000;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Extrai o email (username) do token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Extrai a data de expiração do token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Para extrair qualquer informação do token, precisaremos da chave secreta
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Verifica se o token expirou
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Gera o token para o usuário
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Você pode adicionar mais "claims" (informações) aqui, como os papéis (roles)
        // claims.put("roles", userDetails.getAuthorities());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // Cria o token
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // O "subject" é o username (no seu caso, o email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Valida o token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
