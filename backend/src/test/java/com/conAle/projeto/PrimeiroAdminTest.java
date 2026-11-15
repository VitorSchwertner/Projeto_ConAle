package com.conAle.projeto;

import com.conAle.projeto.Config.PrimeiroAdmin;
import com.conAle.projeto.Model.Usuario;
import com.conAle.projeto.dto.UsuarioRequest;
import com.conAle.projeto.repository.UsuarioRepository;
import com.conAle.projeto.service.UsuarioService;
import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class PrimeiroAdminTest {
    @Test void semConfiguracaoNaoCriaConta() {
        executar(false, "", "", false);
    }
    @Test void adminExistenteNaoTemSenhaSobrescrita() {
        executar(true, "admin@example.invalid", "OutraSenha123!", false);
    }
    @Test void configuracaoValidaCriaPrimeiroAdmin() {
        executar(false, "admin@example.invalid", "TesteSeguro123!", true);
    }
    @Test void vinculaAdminExistentePeloEmailConfiguradoSemSenhaInicial() {
        var repository = mock(UsuarioRepository.class);
        var service = mock(UsuarioService.class);
        var usuario = new Usuario();
        usuario.setAdministrador(true);
        when(repository.existsByAdministradorTrue()).thenReturn(true);
        when(repository.findByEmail("admin@example.invalid")).thenReturn(Optional.of(usuario));

        try (var factory = Validation.buildDefaultValidatorFactory()) {
            new PrimeiroAdmin(repository, service, factory.getValidator(),
                    "Admin", " ADMIN@example.invalid ", "").run(null);
        }

        assertThat(usuario.isAdministradorOriginal()).isTrue();
        verifyNoInteractions(service);
    }
    @Test void naoTrocaAdministradorOriginalQuandoConfiguracaoMuda() {
        var repository = mock(UsuarioRepository.class);
        var service = mock(UsuarioService.class);
        when(repository.existsByAdministradorOriginalTrue()).thenReturn(true);

        try (var factory = Validation.buildDefaultValidatorFactory()) {
            new PrimeiroAdmin(repository, service, factory.getValidator(),
                    "Admin", "segundo@example.invalid", "TesteSeguro123!").run(null);
        }

        verify(repository).existsByAdministradorOriginalTrue();
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(service);
    }
    @Test void senhaUnicodeLongaFalhaSemExporSenha() {
        assertThatThrownBy(() -> executar(false, "admin@example.invalid", "á".repeat(40), false))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Configuração do administrador inicial inválida. Confira nome, e-mail e senha.");
    }
    private void executar(boolean existe, String email, String senha, boolean cria) {
        var repository = mock(UsuarioRepository.class);
        var service = mock(UsuarioService.class);
        when(repository.existsByAdministradorTrue()).thenReturn(existe);
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            new PrimeiroAdmin(repository, service, factory.getValidator(), "Admin", email, senha).run(null);
            if (cria) verify(service).criarAdministradorOriginal(new UsuarioRequest("Admin", email, senha));
            else verifyNoInteractions(service);
        }
    }
}
