package com.conAle.projeto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Registra o codificador de senhas que o service recebe pelo construtor.
@Configuration
public class SegurancaConfig {

    // BCrypt gera um hash diferente a cada chamada, mesmo para a mesma senha.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
