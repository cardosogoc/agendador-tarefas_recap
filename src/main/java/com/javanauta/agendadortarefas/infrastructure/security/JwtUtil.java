package com.javanauta.agendadortarefas.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtUtil {

    //application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    // Extraí validade para constante
    private static final long EXPIRATION = 1000L * 60 * 60; // 1 hora

    // Converte chave Base64 para SecretKey
    private SecretKey getSecretKey() {
        byte[] key = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }

    // Extrai todas as claims
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extrai email do token
    public String extrairEmailToken(String token) {
        return extractClaims(token).getSubject();
    }

    // Verifica expiração
    public boolean isTokenExpired(String token) {
        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // Valida token
    public boolean validateToken(
            String token,
            String email
    ) {
        final String extractedEmail = extrairEmailToken(token);
        return extractedEmail.equals(email) && !isTokenExpired(token);
    }
}