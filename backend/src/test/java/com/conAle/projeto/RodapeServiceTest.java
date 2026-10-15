package com.conAle.projeto;

import com.conAle.projeto.dto.RodapeRequest;
import com.conAle.projeto.service.RodapeService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Usa somente o banco temporario. Cada teste desfaz suas alteracoes.
@SpringBootTest
@Transactional
class RodapeServiceTest {
    @Autowired RodapeService service;
    @Autowired JdbcTemplate jdbc;
    @Autowired EntityManager entityManager;

    @Test
    void consultaSempreORodapeFixo() {
        assertThat(service.buscar().id()).isEqualTo(1);
    }

    @Test
    void atualizaSemCriarOutroRegistroNemApagarLinkLegado() {
        jdbc.update("UPDATE rodape SET link = 'https://example.invalid/legado' WHERE id = 1");
        var resposta = service.atualizar(new RodapeRequest(" 51999999999 ", " ",
                " CONTATO@example.invalid ", " Segunda a sexta ", " 8h as 18h ", " Rua Teste, 123 "));
        // Envia a alteracao do JPA antes das consultas SQL diretas deste teste.
        entityManager.flush();
        assertThat(resposta.id()).isEqualTo(1);
        assertThat(resposta.telefone()).isEqualTo("51999999999");
        assertThat(resposta.email()).isEqualTo("contato@example.invalid");
        assertThat(resposta.instagram()).isNull();
        assertThat(jdbc.queryForObject("SELECT endereco FROM rodape WHERE id = 1", String.class))
                .isEqualTo("Rua Teste, 123");
        assertThat(jdbc.queryForObject("SELECT link FROM rodape WHERE id = 1", String.class))
                .isEqualTo("https://example.invalid/legado");
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM rodape", Integer.class)).isEqualTo(1);
    }

    @Test
    void registroAusenteNaoERecriadoSilenciosamente() {
        jdbc.update("DELETE FROM rodape WHERE id = 1");
        assertThatThrownBy(service::buscar).isInstanceOf(ResponseStatusException.class);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM rodape", Integer.class)).isZero();
    }
}
