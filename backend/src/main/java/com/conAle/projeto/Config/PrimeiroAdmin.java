package com.conAle.projeto.config;

import com.conAle.projeto.dto.UsuarioRequest;
import com.conAle.projeto.repository.UsuarioRepository;
import com.conAle.projeto.service.UsuarioService;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

// Só cria a primeira conta administrativa. Não expõe cadastro público.
@Component
public class PrimeiroAdmin implements ApplicationRunner {
    private final UsuarioRepository repository;
    private final UsuarioService service;
    private final Validator validator;
    private final String nome, email, senha;
    public PrimeiroAdmin(UsuarioRepository repository, UsuarioService service, Validator validator,
            @Value("${ADMIN_INITIAL_NAME:Administrador ConAle}") String nome,
            @Value("${ADMIN_INITIAL_EMAIL:}") String email,
            @Value("${ADMIN_INITIAL_PASSWORD:}") String senha) {
        this.repository = repository; this.service = service; this.validator = validator;
        this.nome = nome; this.email = email; this.senha = senha;
    }
    public void run(ApplicationArguments args) {
        if (repository.existsByAdministradorTrue() || (email.isBlank() && senha.isBlank())) return;
        var dados = new UsuarioRequest(nome, email, senha);
        if (!validator.validate(dados).isEmpty()) {
            throw new IllegalStateException("Configuração do administrador inicial inválida. Confira nome, e-mail e senha.");
        }
        // E-mail existente causa erro sem promover nem sobrescrever a conta antiga.
        service.criar(dados);
    }
}
