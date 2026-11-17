
package com.conAle.projeto.Config;

import com.conAle.projeto.Model.Usuario;
import com.conAle.projeto.dto.UsuarioRequest;
import com.conAle.projeto.repository.UsuarioRepository;
import com.conAle.projeto.service.UsuarioService;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Component
public class PrimeiroAdmin implements ApplicationRunner {

    private final UsuarioRepository repository;
    private final UsuarioService service;
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;

    private final String nome;
    private final String email;
    private final String senha;
    private final String senhaRedefinicao;

    public PrimeiroAdmin(
            UsuarioRepository repository,
            UsuarioService service,
            Validator validator,
            PasswordEncoder passwordEncoder,
            @Value("${ADMIN_INITIAL_NAME:Administrador ConAle}") String nome,
            @Value("${ADMIN_INITIAL_EMAIL:}") String email,
            @Value("${ADMIN_INITIAL_PASSWORD:}") String senha,
            @Value("${ADMIN_RESET_PASSWORD:}") String senhaRedefinicao
    ) {
        this.repository = repository;
        this.service = service;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.senhaRedefinicao = senhaRedefinicao;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {

        // Redefine a senha do administrador original, se solicitado.
        if (!senhaRedefinicao.isBlank()) {

            String emailInicial = email.trim().toLowerCase(Locale.ROOT);

            Usuario usuario = repository.findByEmail(emailInicial)
                    .orElseThrow(() -> new IllegalStateException(
                            "Administrador não encontrado para redefinição de senha."
                    ));

            if (!usuario.isAdministradorOriginal()) {
                throw new IllegalStateException(
                        "O usuário informado não é o administrador original."
                );
            }

            usuario.setSenhaHash(
                    passwordEncoder.encode(senhaRedefinicao)
            );

            repository.save(usuario);

            return;
        }

        // Evita cadastrar outro administrador original.
        if (repository.existsByAdministradorOriginalTrue()
                || (email.isBlank() && senha.isBlank())) {
            return;
        }

        String emailInicial = email.trim().toLowerCase(Locale.ROOT);

        // Se já existir um administrador, apenas marca como original.
        if (repository.existsByAdministradorTrue()) {

            repository.findByEmail(emailInicial)
                    .filter(Usuario::isAdministrador)
                    .ifPresent(usuario ->
                            usuario.setAdministradorOriginal(true)
                    );

            return;
        }

        // Cria o primeiro administrador.
        var dados = new UsuarioRequest(nome, emailInicial, senha);

        if (!validator.validate(dados).isEmpty()) {
            throw new IllegalStateException(
                    "Configuração do administrador inicial inválida. "
                            + "Verifique as variáveis ADMIN_INITIAL_*."
            );
        }

        service.criarAdministradorOriginal(dados);
    }
}
