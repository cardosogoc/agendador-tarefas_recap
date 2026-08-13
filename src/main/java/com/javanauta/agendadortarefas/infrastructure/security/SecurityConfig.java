package com.javanauta.agendadortarefas.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Adicionados atributos da classe para guardar dependências
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    // Spring moderno já injeta automaticamente quando existe um único construtor
    public SecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Cria uma instância do filtro JWT com JwtUtil e UserDetailsService
        JwtRequestFilter jwtRequestFilter = new JwtRequestFilter(jwtUtil, userDetailsService);

        return http
                // Desativa proteção CSRF para APIs REST
                // (normal em autenticação JWT stateless)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest()
                        .authenticated()
                )
                // Adiciona filtro JWT antes do filtro padrão
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

}