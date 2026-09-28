package com.conAle.projeto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Ativa o Spring Boot e a descoberta de componentes deste pacote e subpacotes.
@SpringBootApplication
public class ConaleApplication {
    // Ponto de entrada: inicia a aplicacao e suas configuracoes.
    public static void main(String[] args) {
        SpringApplication.run(ConaleApplication.class, args);
    }
}
