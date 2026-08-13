package com.javanauta.agendadortarefas.infrastructure.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    // Construtor
    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Executado uma vez por requisição
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            // Obtém Authorization
            final String authorizationHeader =
                    request.getHeader("Authorization");

            // Verifica Bearer Token
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
            ) {// Extrai token
                final String token = authorizationHeader.substring(7);

                // username → email
                final String email = jwtUtil.extrairEmailToken(token);
                // Verifica autenticação
                if (email != null && SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {
                    // Busca usuário
                    UserDetails userDetails = userDetailsService.carregaDadosUsuario(email, authorizationHeader);
                    // Valida token
                    if (jwtUtil.validateToken(token, email)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails,null, userDetails.getAuthorities());
                        // Define autenticação
                        SecurityContextHolder
                                .getContext()
                                .setAuthentication(authentication);
                    }
                }
            }
            // Continua fluxo
            chain.doFilter(request, response);
        }
        // Token expirado
        catch (ExpiredJwtException e) {

            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    buildError(HttpStatus.UNAUTHORIZED.value(),"Token expirado", request.getRequestURI()));
        }


        // Outros erros JWT
        catch (JwtException e) {
            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(
                    "application/json"
            );
            response.getWriter().write(
                    buildError(
                            HttpStatus.UNAUTHORIZED.value(), "Token inválido",
                            request.getRequestURI())
            );
        }
    }

    private String buildError(
            int status,
            String message,
            String path
    ) {

        return """
                {
                  "timestamp":"%s",
                  "status":%d,
                  "message":"%s",
                  "path":"%s"
                }
                """
                .formatted(
                        LocalDateTime.now(),
                        status,
                        message,
                        path
                );
    }
}