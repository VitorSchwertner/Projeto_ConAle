package com.conAle.projeto;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import java.sql.SQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Inicia o Spring com um MySQL de testes; nunca apontar para o banco real.
// @Transactional desfaz as alteracoes de cada teste que usa essa anotacao.
@SpringBootTest
class DatabaseMigrationTest {
    // O Spring fornece o acesso SQL e o controle das migrations.
    @Autowired JdbcTemplate jdbc;
    @Autowired Flyway flyway;

    @Test
    // Confere a versão e garante que o Flyway não reaplique migrations.
    void migrationCanRunAgainWithoutChangingSchemaOrFooter() {
        assertThat(flyway.info().current().getVersion().getVersion()).isEqualTo("17");
        flyway.validate();
        assertThat(flyway.migrate().migrationsExecuted).isZero();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM rodape WHERE id = 1", Integer.class)).isEqualTo(1);
        assertThat(jdbc.queryForList("SHOW TABLES", String.class))
                .contains("usuario", "banner", "noticia", "rodape", "indicador_institucional",
                        "mensagens_clientes", "rodape_ativo", "link_site", "flyway_schema_history");
    }

    @Test
    void cadastraIndicadorInicialComValoresZerados() {
        assertThat(jdbc.queryForList(
                "SELECT CONCAT_WS('|', anos_atuacao, empresas_abertas) FROM indicador_institucional",
                String.class)).containsExactly("0|0");
    }

    @Test
    void cadastraAvaliacoesIniciaisComCincoEstrelas() {
        assertThat(jdbc.queryForList("""
                SELECT CONCAT_WS('|', titulo, area_atuacao)
                FROM mensagens_clientes
                WHERE nota = 5 AND CHAR_LENGTH(mensagem) > 0
                ORDER BY id DESC
                """, String.class)).containsExactly(
                "Ana|Fisioterapeuta",
                "Vanessa e Thais|Fisioterapeutas",
                "Janaína|Psicóloga",
                "Flávia|Terapeuta",
                "Gabriela|Médica");
    }

    @Test
    void cadastraCardsDaHomeNaOrdemOriginal() {
        assertThat(jdbc.queryForList("SELECT titulo FROM noticia ORDER BY id DESC", String.class))
                .containsSubsequence(
                        "Como acessar a área do cliente?",
                        "Como funciona o serviço de BPO Financeiro?",
                        "O que preciso para abrir um CNPJ?",
                        "Como sei se estou pagando impostos corretamente?",
                        "Posso trocar de contador facilmente?",
                        "Como funciona o atendimento da ConAle?");
    }

    @Test
    @Transactional
    void cadaPosicaoPermiteApenasUmBannerAtivo() {
        jdbc.update("INSERT INTO banner(titulo, imagem, posicao, ativo) VALUES ('Rascunho', '/teste.png', 'HOME_TOPO', FALSE)");
        assertMysqlError(1062, () -> jdbc.update("UPDATE banner SET ativo = TRUE WHERE titulo = 'Rascunho'"));
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM banner WHERE posicao = 'HOME_TOPO' AND ativo = TRUE",
                Integer.class)).isEqualTo(1);
    }

    @Test
    void cadastraBannersPadraoAtivosComAsArtesDaPosicao() {
        assertThat(jdbc.queryForList("""
                SELECT CONCAT_WS('|', posicao, imagem, imagem_mobile)
                FROM banner WHERE ativo = TRUE
                """, String.class)).contains(
                "HOME_TOPO|/banners/padrao/home-desktop.png|/banners/padrao/home-mobile.png",
                "SERVICOS_TOPO|/banners/padrao/servicos-desktop.png|/banners/padrao/servicos-mobile.png",
                "SOBRE_NOS|/banners/padrao/sobre-nos-desktop.png|/banners/padrao/sobre-nos-mobile.png",
                "AREA_CLIENTE|/banners/padrao/area-do-cliente.png",
                "SOBRE_MIM_FOTO|/banners/padrao/sobre-mim-desktop.png|/banners/padrao/sobre-mim-mobile.png");
    }

    @Test
    @Transactional
    // Rejeita email repetido e usuario sem hash de senha.
    void userEmailMustBeUniqueAndPasswordHashRequired() {
        jdbc.update("INSERT INTO usuario(nome, email, senha_hash) VALUES (?, ?, ?)",
                "Teste", "teste@example.invalid", "hash-apenas-para-teste");
        assertThat(jdbc.queryForObject("SELECT administrador FROM usuario WHERE email = 'teste@example.invalid'",
                Boolean.class)).isFalse();
        assertMysqlError(1062, () -> jdbc.update(
                "INSERT INTO usuario(nome, email, senha_hash) VALUES (?, ?, ?)",
                "Outro", "TESTE@example.invalid", "outro-hash"));
        assertMysqlError(1364, () -> jdbc.update(
                "INSERT INTO usuario(nome, email) VALUES (?, ?)", "Sem senha", "sem@example.invalid"));
    }

    @Test
    @Transactional
    void administradorOriginalDeveSerUnicoEManterPapelAdministrativo() {
        jdbc.update("INSERT INTO usuario(nome, email, senha_hash, administrador, administrador_original) "
                + "VALUES ('Original', 'original@example.invalid', 'hash', 1, 1)");
        assertMysqlError(1062, () -> jdbc.update(
                "INSERT INTO usuario(nome, email, senha_hash, administrador, administrador_original) "
                        + "VALUES ('Outro', 'outro@example.invalid', 'hash', 1, 1)"));
        assertMysqlError(3819, () -> jdbc.update(
                "UPDATE usuario SET administrador = 0 WHERE email = 'original@example.invalid'"));
    }

    @Test
    @Transactional
    // O padrao SQL agora e ativo; o CHECK continua aceitando apenas 0 ou 1.
    void bannerStartsActiveAndRequiresImageAndTitle() {
        jdbc.update("INSERT INTO banner(titulo, imagem) VALUES ('Banner teste', '/teste.png')");
        assertThat(jdbc.queryForObject("SELECT ativo FROM banner WHERE titulo = 'Banner teste'", Boolean.class)).isTrue();
        assertThat(jdbc.queryForObject("SELECT posicao FROM banner WHERE titulo = 'Banner teste'", String.class)).isNull();
        assertMysqlError(1364, () -> jdbc.update("INSERT INTO banner(titulo) VALUES ('Sem imagem')"));
        assertMysqlError(1364, () -> jdbc.update("INSERT INTO banner(imagem) VALUES ('/sem-titulo.png')"));
        assertMysqlError(3819, () -> jdbc.update("UPDATE banner SET ativo = 2"));
        assertMysqlError(3819, () -> jdbc.update("UPDATE banner SET posicao = 'INVALIDA' WHERE titulo = 'Banner teste'"));
    }

    @Test
    @Transactional
    // Permite noticia sem subtitulo, mas nunca sem titulo.
    void newsAcceptsMissingSubtitleButRequiresTitle() {
        jdbc.update("INSERT INTO noticia(titulo) VALUES ('Noticia teste')");
        assertThat(jdbc.queryForObject("SELECT sub_titulo FROM noticia WHERE titulo = 'Noticia teste'", String.class)).isNull();
        assertMysqlError(1364, () -> jdbc.update("INSERT INTO noticia(sub_titulo) VALUES ('Sem titulo')"));
    }

    @Test
    @Transactional
    void rodapePadraoFicaAtivoEProtegidoNoBanco() {
        assertThat(jdbc.queryForObject("SELECT nome FROM rodape WHERE id = 1", String.class)).isEqualTo("Padrão");
        assertThat(jdbc.queryForObject("SELECT rodape_id FROM rodape_ativo WHERE id = 1", Integer.class)).isEqualTo(1);
        assertMysqlError(3819, () -> jdbc.update("INSERT INTO rodape_ativo(id, rodape_id) VALUES (2, 1)"));
        assertMysqlError(1451, () -> jdbc.update("DELETE FROM rodape WHERE id = 1"));
    }

    @Test
    void rodapeMantemEnderecoEGeraIdParaNovosCadastros() {
        assertThat(jdbc.queryForList("SELECT COLUMN_NAME FROM information_schema.COLUMNS "
                + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'rodape'", String.class))
                .contains("endereco", "link").doesNotContain("local");
        assertThat(jdbc.queryForObject("SELECT EXTRA FROM information_schema.COLUMNS "
                + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'rodape' AND COLUMN_NAME = 'id'",
                String.class)).contains("auto_increment");
    }

    @Test
    @Transactional
    // Confere escrita nas duas novas tabelas e seus campos obrigatorios.
    void newContentTablesAcceptValidRecords() {
        jdbc.update("INSERT INTO indicador_institucional(anos_atuacao, empresas_abertas) VALUES (10, 100)");
        jdbc.update("INSERT INTO mensagens_clientes(titulo, area_atuacao, nota, mensagem) "
                + "VALUES ('Cliente teste', 'Comercio', 4.5, 'Atendimento teste')");
        assertThat(jdbc.queryForObject("SELECT nota FROM mensagens_clientes WHERE titulo = 'Cliente teste'",
                Double.class)).isEqualTo(4.5);
        assertMysqlError(1364, () -> jdbc.update("INSERT INTO indicador_institucional(anos_atuacao) VALUES (1)"));
        assertMysqlError(1364, () -> jdbc.update("INSERT INTO mensagens_clientes(titulo) VALUES ('Incompleto')"));
    }

    // Confere o motivo da rejeicao, nao apenas a existencia de algum erro.
    // 1062: duplicado; 1364: campo obrigatorio ausente; 3819: CHECK violado.
    private void assertMysqlError(int code, Runnable operation) {
        assertThatThrownBy(operation::run)
                .isInstanceOf(DataAccessException.class)
                .rootCause()
                .isInstanceOf(SQLException.class)
                .extracting(error -> ((SQLException) error).getErrorCode())
                .isEqualTo(code);
    }
}
