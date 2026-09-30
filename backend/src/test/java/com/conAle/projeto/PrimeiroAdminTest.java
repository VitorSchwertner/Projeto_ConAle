package com.conAle.projeto;

import com.conAle.projeto.config.PrimeiroAdmin;
import com.conAle.projeto.dto.UsuarioRequest;
import com.conAle.projeto.repository.UsuarioRepository;
import com.conAle.projeto.service.UsuarioService;
import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;
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
            if (cria) verify(service).criar(new UsuarioRequest("Admin", email, senha));
            else verifyNoInteractions(service);
        }
    }
}
