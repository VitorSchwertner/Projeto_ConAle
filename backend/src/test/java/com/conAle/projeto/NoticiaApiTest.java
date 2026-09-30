package com.conAle.projeto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Executar com compose.test.yaml: ele fornece um MySQL temporário para os testes.
// Carrega a aplicação, simula chamadas HTTP e desfaz os dados ao fim de cada teste.
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@org.springframework.security.test.context.support.WithMockUser(roles = "ADMIN")
class NoticiaApiTest {

    @Autowired
    // Chama as rotas pelo Spring sem precisar abrir uma porta HTTP.
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    // Monta JSON a partir de dados Java, sem concatenar textos manualmente.
    private ObjectMapper objectMapper;

    // Os testes de notícias usam um administrador e CSRF válido por padrão.
    @org.junit.jupiter.api.BeforeEach
    void configurarCsrf(@Autowired org.springframework.web.context.WebApplicationContext context) {
        mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup(context)
                .apply(org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity())
                .defaultRequest(get("/").with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()))
                .build();
    }

    @Test
    // Confere o contrato do cadastro: status 201, Location e campos da resposta.
    void deveCadastrarNoticia() throws Exception {
        String json = """
                {
                    "titulo": "Novidades da ConAle",
                    "sub_titulo": "Confira as novidades."
                }
                """;

        mockMvc.perform(
                        post("/api/noticias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.titulo")
                        .value("Novidades da ConAle"))
                .andExpect(jsonPath("$.sub_titulo")
                        .value("Confira as novidades."));
    }
    @Test
    // Percorre o CRUD usando o endereço retornado, sem depender de um ID fixo.
    void deveConsultarEditarEExcluirNoticia() throws Exception {
        String localizacao = mockMvc.perform(
                        post("/api/noticias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "titulo": "Título inicial",
                                        "sub_titulo": "Subtítulo inicial"
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        assertNotNull(localizacao);
        sincronizarBanco();
        mockMvc.perform(get(localizacao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Título inicial"));

        mockMvc.perform(
                        put(localizacao)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "titulo": "Título atualizado",
                                        "sub_titulo": "Subtítulo atualizado"
                                    }
                                    """)
                )
                .andExpect(status().isOk());

        sincronizarBanco();
        mockMvc.perform(get(localizacao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Título atualizado"))
                .andExpect(jsonPath("$.sub_titulo")
                        .value("Subtítulo atualizado"));

        mockMvc.perform(delete(localizacao))
                .andExpect(status().isNoContent());

        sincronizarBanco();
        mockMvc.perform(get(localizacao))
                .andExpect(status().isNotFound());
    }
    @Test
    // Um texto só com espaços também deve ser considerado inválido.
    void deveRejeitarTituloEmBranco() throws Exception {
        mockMvc.perform(
                        post("/api/noticias")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "titulo": "   ",
                                        "sub_titulo": "Subtítulo válido"
                                    }
                                    """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.titulo").exists());
    }

    @Test
    // Procura o ID criado na lista; não depende da quantidade total de registros.
    void deveListarNoticiaCadastrada() throws Exception {
        String localizacao = cadastrarNoticia("Notícia da listagem", "Resumo");
        int id = Integer.parseInt(localizacao.substring(localizacao.lastIndexOf('/') + 1));

        sincronizarBanco();

        mockMvc.perform(get("/api/noticias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", hasItem(id)));
    }

    @Test
    // Confere que o campo opcional continua null ao consultar o banco.
    void deveCadastrarSemSubtitulo() throws Exception {
        String localizacao = mockMvc.perform(post("/api/noticias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo": "Somente título"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sub_titulo").value(nullValue()))
                .andReturn().getResponse().getHeader("Location");

        assertNotNull(localizacao);
        sincronizarBanco();

        mockMvc.perform(get(localizacao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Somente título"))
                .andExpect(jsonPath("$.sub_titulo").value(nullValue()));
    }

    @Test
    // Documenta a regra do PUT: omitir o subtítulo apaga o valor anterior.
    void deveRemoverSubtituloQuandoOmitidoNaEdicao() throws Exception {
        String localizacao = cadastrarNoticia("Título original", "Subtítulo original");
        sincronizarBanco();

        mockMvc.perform(put(localizacao)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo": "Título atualizado"}
                                """))
                .andExpect(status().isOk());

        sincronizarBanco();

        mockMvc.perform(get(localizacao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Título atualizado"))
                .andExpect(jsonPath("$.sub_titulo").value(nullValue()));
    }

    @Test
    // A ausência do título deve gerar erro no campo correspondente.
    void deveRejeitarTituloAusente() throws Exception {
        mockMvc.perform(post("/api/noticias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"sub_titulo": "Sem título"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.titulo").exists());
    }

    @Test
    // Testa um caractere além do limite de 200.
    void deveRejeitarTituloAcimaDoLimite() throws Exception {
        String json = objectMapper.writeValueAsString(Map.of("titulo", "a".repeat(201)));

        mockMvc.perform(post("/api/noticias")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.titulo").exists());
    }

    @Test
    // Testa um caractere além do limite de 500.
    void deveRejeitarSubtituloAcimaDoLimite() throws Exception {
        String json = objectMapper.writeValueAsString(Map.of(
                "titulo", "Título válido", "sub_titulo", "a".repeat(501)));

        mockMvc.perform(post("/api/noticias")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.sub_titulo").exists());
    }

    @Test
    // Confere que os tamanhos máximos permitidos são salvos sem cortar o texto.
    void deveAceitarCamposNoLimite() throws Exception {
        String titulo = "a".repeat(200);
        String subTitulo = "b".repeat(500);
        String localizacao = cadastrarNoticia(titulo, subTitulo);
        sincronizarBanco();

        mockMvc.perform(get(localizacao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value(titulo))
                .andExpect(jsonPath("$.sub_titulo").value(subTitulo));
    }

    @Test
    // Consulta, edição e exclusão devem indicar quando o registro não existe.
    void deveRetornar404ParaIdInexistente() throws Exception {
        // O cadastro com AUTO_INCREMENT não gera o identificador zero.
        String rota = "/api/noticias/0";

        mockMvc.perform(get(rota)).andExpect(status().isNotFound());
        mockMvc.perform(put(rota)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo": "Título válido"}
                                """))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete(rota)).andExpect(status().isNotFound());
    }

    @Test
    // Além do erro 400, verifica que nenhum dos dados anteriores foi alterado.
    void devePreservarDadosQuandoEdicaoForInvalida() throws Exception {
        String localizacao = cadastrarNoticia("Título original", "Subtítulo original");
        sincronizarBanco();

        mockMvc.perform(put(localizacao)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "titulo": "   ",
                                    "sub_titulo": "Não deve ser salvo"
                                }
                                """))
                .andExpect(status().isBadRequest());

        sincronizarBanco();

        mockMvc.perform(get(localizacao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Título original"))
                .andExpect(jsonPath("$.sub_titulo").value("Subtítulo original"));
    }

    // Envia as alterações ao MySQL e evita que a próxima busca use a entidade
    // em memória. Não realiza commit: o teste ainda desfaz tudo ao terminar.
    private void sincronizarBanco() {
        entityManager.flush();
        entityManager.clear();
    }

    // Auxiliar para criar dados de teste e devolver o endereço da notícia.
    // Aqui o subtítulo deve ser não nulo; o teste de campo ausente monta outro JSON.
    private String cadastrarNoticia(String titulo, String subTitulo) throws Exception {
        String json = objectMapper.writeValueAsString(Map.of(
                "titulo", titulo, "sub_titulo", subTitulo));

        String localizacao = mockMvc.perform(post("/api/noticias")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        assertNotNull(localizacao);
        return localizacao;
    }
}
