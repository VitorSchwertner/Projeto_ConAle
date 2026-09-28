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
    // Confere as tabelas e garante que a V1 nao seja aplicada novamente.
    void migrationCanRunAgainWithoutChangingSchemaOrFooter() {
        assertThat(flyway.info().current().getVersion().getVersion()).isEqualTo("1");
        flyway.validate();
        assertThat(flyway.migrate().migrationsExecuted).isZero();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM rodape WHERE id = 1", Integer.class)).isEqualTo(1);
        assertThat(jdbc.queryForList("SHOW TABLES", String.class))
                .contains("usuario", "banner", "noticia", "rodape", "flyway_schema_history");
    }

    @Test
    @Transactional
    // Rejeita email repetido e usuario sem hash de senha.
    void userEmailMustBeUniqueAndPasswordHashRequired() {
        jdbc.update("INSERT INTO usuario(nome, email, senha_hash) VALUES (?, ?, ?)",
                "Teste", "teste@example.invalid", "hash-apenas-para-teste");
        assertMysqlError(1062, () -> jdbc.update(
                "INSERT INTO usuario(nome, email, senha_hash) VALUES (?, ?, ?)",
                "Outro", "TESTE@example.invalid", "outro-hash"));
        assertMysqlError(1364, () -> jdbc.update(
                "INSERT INTO usuario(nome, email) VALUES (?, ?)", "Sem senha", "sem@example.invalid"));
    }

    @Test
    @Transactional
    // Exige titulo e imagem; ativo comeca falso e aceita apenas 0 ou 1.
    void bannerStartsInactiveAndRequiresImageAndTitle() {
        jdbc.update("INSERT INTO banner(titulo, imagem) VALUES ('Banner teste', '/teste.png')");
        assertThat(jdbc.queryForObject("SELECT ativo FROM banner WHERE titulo = 'Banner teste'", Boolean.class)).isFalse();
        assertMysqlError(1364, () -> jdbc.update("INSERT INTO banner(titulo) VALUES ('Sem imagem')"));
        assertMysqlError(1364, () -> jdbc.update("INSERT INTO banner(imagem) VALUES ('/sem-titulo.png')"));
        assertMysqlError(3819, () -> jdbc.update("UPDATE banner SET ativo = 2"));
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
    // Permite editar o rodape, mas impede outro registro ou outro id.
    void footerCanBeEditedButCannotHaveAnotherIdOrDuplicate() {
        jdbc.update("UPDATE rodape SET telefone = '51999999999' WHERE id = 1");
        assertThat(jdbc.queryForObject("SELECT telefone FROM rodape WHERE id = 1", String.class)).isEqualTo("51999999999");
        assertMysqlError(3819, () -> jdbc.update("INSERT INTO rodape(id) VALUES (2)"));
        assertMysqlError(1062, () -> jdbc.update("INSERT INTO rodape(id) VALUES (1)"));
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
