package com.conAle.projeto.Config;

import com.conAle.projeto.dto.UsuarioRequest;
import com.conAle.projeto.repository.UsuarioRepository;
import com.conAle.projeto.service.UsuarioService;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

// Vincula ao e-mail inicial a identidade permanente do administrador original.
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
    @Transactional
    public void run(ApplicationArguments args) {
        if (repository.existsByAdministradorOriginalTrue() || (email.isBlank() && senha.isBlank())) return;

        String emailInicial = email.trim().toLowerCase(Locale.ROOT);
        if (repository.existsByAdministradorTrue()) {
            repository.findByEmail(emailInicial).filter(usuario -> usuario.isAdministrador())
                    .ifPresent(usuario -> usuario.setAdministradorOriginal(true));
            return;
        }

        var dados = new UsuarioRequest(nome, emailInicial, senha);
        if (!validator.validate(dados).isEmpty()) {
            throw new IllegalStateException("Configuração do administrador inicial inválida. Confira nome, e-mail e senha.");
        }
        // E-mail existente causa erro sem promover nem sobrescrever a conta antiga.
        service.criarAdministradorOriginal(dados);
    }
}
