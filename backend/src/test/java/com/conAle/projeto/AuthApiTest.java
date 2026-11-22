package com.conAle.projeto;

import com.conAle.projeto.Model.Usuario;
import com.conAle.projeto.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

// Usa MySQL temporário e desfaz os registros no fim de cada teste.
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthApiTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired UsuarioRepository usuarios;
    @Autowired PasswordEncoder encoder;
    Usuario admin;
    static final String EMAIL = "admin-auth@example.invalid";
    static final String SENHA = "TesteSeguro123!";

    @BeforeEach void preparar() {
        admin = new Usuario(); admin.setNome("Admin Teste"); admin.setEmail(EMAIL);
        admin.setSenhaHash(encoder.encode(SENHA)); admin.setAdministrador(true);
        usuarios.saveAndFlush(admin);
    }

    String dados(String email, String senha) throws Exception {
        return json.writeValueAsString(Map.of("email", email, "senha", senha));
    }
    MockHttpSession login() throws Exception {
        return login(EMAIL, SENHA);
    }
    MockHttpSession login(String email, String senha) throws Exception {
        return (MockHttpSession) mvc.perform(post("/api/auth/login").with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(dados(email, senha)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.senhaHash").doesNotExist())
                .andExpect(jsonPath("$.senha").doesNotExist())
                .andReturn().getRequest().getSession(false);
    }

    @Test void linksDoSiteSaoPublicosEEdicaoExigeSessao() throws Exception {
        String alteracao = """
                {"acao-diagnostico":"https://example.invalid/agendar","servico-bpo":"/servicos#lista-servicos"}
                """;
        mvc.perform(get("/api/links-site")).andExpect(status().isOk());
        mvc.perform(patch("/api/links-site").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(alteracao))
                .andExpect(status().isUnauthorized());

        var session = login();
        mvc.perform(patch("/api/links-site").session(session).contentType(MediaType.APPLICATION_JSON)
                        .content(alteracao))
                .andExpect(status().isForbidden());
        mvc.perform(patch("/api/links-site").session(session).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(alteracao))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/links-site")).andExpect(status().isOk())
                .andExpect(jsonPath("$['acao-diagnostico']").value("https://example.invalid/agendar"))
                .andExpect(jsonPath("$['servico-bpo']").value("/servicos#lista-servicos"));

        mvc.perform(patch("/api/links-site").session(session).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"acao-diagnostico":"/contato","servico-bpo":"javascript:void(0)"}
                                """))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/links-site"))
                .andExpect(jsonPath("$['acao-diagnostico']").value("https://example.invalid/agendar"));

        mvc.perform(patch("/api/links-site").session(session).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"acao-diagnostico":""}
                                """))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/links-site")).andExpect(status().isOk())
                .andExpect(jsonPath("$['acao-diagnostico']").doesNotExist())
                .andExpect(jsonPath("$['servico-bpo']").value("/servicos#lista-servicos"));
    }

    @Test void loginMantemSessao() throws Exception {
        var session = login();
        mvc.perform(get("/api/auth/me").session(session)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(admin.getId()))
                .andExpect(jsonPath("$.administrador").value(true));
        mvc.perform(get("/api/usuarios").session(session)).andExpect(status().isOk());
    }
    @Test void noticiasSaoPublicasUsuariosNao() throws Exception {
        mvc.perform(get("/api/noticias")).andExpect(status().isOk());
        mvc.perform(get("/api/banners/ativos")).andExpect(status().isOk());
        mvc.perform(get("/api/rodape")).andExpect(status().isOk())
                .andExpect(jsonPath("$.telefone").value("+55 (51) 99661-9470"))
                .andExpect(jsonPath("$.instagram").value("@conale.consultoria"))
                .andExpect(jsonPath("$.email").value("atendimento.conale@gmail.com"))
                .andExpect(jsonPath("$.diasSemana").value("Segunda à Sexta-feira"))
                .andExpect(jsonPath("$.horasTrabalhadas").value("08:00h - 12:00h | 13:00h - 17:15h"))
                .andExpect(jsonPath("$.endereco").value("R. Guanabara, 353 - Sala 1, São Cristóvão, Lajeado - RS, 95913-122"));
        mvc.perform(get("/api/indicadores-institucionais")).andExpect(status().isOk());
        mvc.perform(get("/api/mensagens-clientes")).andExpect(status().isOk());
        mvc.perform(get("/api/banners")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/usuarios")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/noticias").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Bloqueado\"}")).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/usuarios").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andExpect(status().isUnauthorized());
    }
    @Test void loginRejeitaSenhaErradaEEmailDesconhecidoIgualmente() throws Exception {
        for (var email : new String[]{EMAIL, "inexistente@example.invalid"}) {
            mvc.perform(post("/api/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                    .content(dados(email, "Incorreta123!"))).andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.detail").value("E-mail ou senha inválidos."));
        }
    }
    @Test void gerenciaModelosEPublicaSomenteORodapeAtivo() throws Exception {
        String rota = "/api/rodape/modelos";
        var session = login();
        mvc.perform(get(rota)).andExpect(status().isUnauthorized());
        mvc.perform(get(rota).session(session)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Padrão"))
                .andExpect(jsonPath("$[0].ativo").value(true));

        var dados = new java.util.HashMap<>(Map.of(
                "telefone", "51999999999", "email", "plantao@example.invalid",
                "diasSemana", "Sábado", "horasTrabalhadas", "8h às 12h",
                "endereco", "Rua do Plantão, 100"));
        String payload = json.writeValueAsString(Map.of("nome", "Plantão", "dados", dados));
        mvc.perform(post(rota).session(session).contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isForbidden());
        var criado = mvc.perform(post(rota).session(session).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ativo").value(false))
                .andReturn().getResponse().getContentAsString();
        int id = json.readTree(criado).path("dados").path("id").asInt();
        mvc.perform(get("/api/rodape")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").doesNotExist());

        dados.put("email", "novo-plantao@example.invalid");
        payload = json.writeValueAsString(Map.of("nome", "Plantão especial", "dados", dados));
        mvc.perform(put(rota + "/" + id).session(session).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Plantão especial"));

        mvc.perform(put(rota + "/" + id + "/ativacao").session(session).with(csrf()))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/rodape")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").value("novo-plantao@example.invalid"));
        mvc.perform(delete(rota + "/" + id).session(session).with(csrf()))
                .andExpect(status().isConflict());
        mvc.perform(get(rota).session(session)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ativo").value(false))
                .andExpect(jsonPath("$[1].ativo").value(true));
        mvc.perform(delete(rota + "/1").session(session).with(csrf()))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/rodape")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test void designEntraGerenciaConteudoMasNaoUsuarios() throws Exception {
        admin.setAdministrador(false); usuarios.saveAndFlush(admin);
        var session = login();
        mvc.perform(get("/api/auth/me").session(session)).andExpect(status().isOk())
                .andExpect(jsonPath("$.administrador").value(false));
        mvc.perform(get("/api/usuarios").session(session)).andExpect(status().isForbidden());
        mvc.perform(post("/api/noticias").session(session).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Notícia do Design\"}"))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/banners").session(session).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Banner\",\"imagem\":\"/banner.png\","
                        + "\"posicao\":\"HOME_TOPO\",\"ativo\":false}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.posicao").value("HOME_TOPO"));
        mvc.perform(post("/api/indicadores-institucionais").session(session).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"anosAtuacao\":10,\"empresasAbertas\":20}"))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/mensagens-clientes").session(session).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Cliente\",\"areaAtuacao\":\"Saúde\",\"nota\":5,\"mensagem\":\"Ótimo\"}"))
                .andExpect(status().isCreated());
        mvc.perform(put("/api/rodape").session(session).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"telefone\":\"51999999999\",\"email\":\"contato@example.invalid\","
                        + "\"diasSemana\":\"Segunda a sexta\",\"horasTrabalhadas\":\"8h às 18h\","
                        + "\"endereco\":\"Rua de Teste, 1\"}"))
                .andExpect(status().isOk());
    }
    @Test void csrfObrigatorioInclusiveNoLogin() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(dados(EMAIL, SENHA))).andExpect(status().isForbidden());
        mvc.perform(post("/api/noticias").session(login()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Bloqueado\"}")).andExpect(status().isForbidden());
    }

    @Test void linksPublicosRejeitamRotasEAncorasInvalidas() throws Exception {
        var session = login();
        for (String link : java.util.List.of("www.example.invalid", "/rota-inexistente", "/sobre#faq", "#secao-inexistente")) {
            mvc.perform(patch("/api/links-site").session(session).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json.writeValueAsString(Map.of("acao-diagnostico", link))))
                    .andExpect(status().isBadRequest());
            mvc.perform(post("/api/banners").session(session).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json.writeValueAsString(Map.of(
                                    "titulo", "Rascunho", "imagem", "/banner.png", "posicao", "HOME_TOPO",
                                    "ativo", false, "link", link))))
                    .andExpect(status().isBadRequest());
        }
        mvc.perform(patch("/api/links-site").session(session).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of("acao-diagnostico", "/duvidas#faq",
                                "acao-reuniao", "#lista-servicos"))))
                .andExpect(status().isNoContent());
    }

    @Test void bannerExigePosicaoValida() throws Exception {
        var session = login();
        mvc.perform(post("/api/banners").session(session).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Sem posição\",\"imagem\":\"/banner.png\"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/banners").session(session).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Posição inválida\",\"imagem\":\"/banner.png\","
                        + "\"posicao\":\"QUALQUER\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test void linkDoBannerFicaDisponivelSomenteNasPosicoesComChamada() throws Exception {
        var session = login();
        var dados = new java.util.HashMap<>(Map.of(
                "titulo", "Banner com chamada",
                "imagem", "/banner.png",
                "posicao", "SOBRE_NOS",
                "link", "https://example.invalid/destino",
                "ativo", false));

        var criado = mvc.perform(post("/api/banners").session(session).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(dados)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.link").value(dados.get("link")))
                .andReturn();

        String localizacao = criado.getResponse().getHeader("Location");
        dados.put("posicao", "SOBRE_MIM_FOTO");
        mvc.perform(put(localizacao).session(session).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(dados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.link").doesNotExist());

        mvc.perform(get(localizacao).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.link").doesNotExist());
    }

    @Test void tokenRealTrocaAoEntrarESessaoTrocaId() throws Exception {
        var initial = mvc.perform(get("/api/auth/csrf")).andExpect(status().isOk()).andReturn();
        var session = (MockHttpSession) initial.getRequest().getSession(false);
        String oldId = session.getId();
        String token = json.readTree(initial.getResponse().getContentAsString()).get("token").asText();
        mvc.perform(post("/api/auth/login").session(session).header("X-CSRF-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON).content(dados(EMAIL, SENHA)))
                .andExpect(status().isOk());
        assertThat(session.getId()).isNotEqualTo(oldId);
        mvc.perform(post("/api/noticias").session(session).header("X-CSRF-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON).content("{\"titulo\":\"Antigo\"}"))
                .andExpect(status().isForbidden());
        var fresh = mvc.perform(get("/api/auth/csrf").session(session)).andReturn();
        String novo = json.readTree(fresh.getResponse().getContentAsString()).get("token").asText();
        mvc.perform(post("/api/noticias").session(session).header("X-CSRF-TOKEN", novo)
                .contentType(MediaType.APPLICATION_JSON).content("{\"titulo\":\"Autorizado\"}"))
                .andExpect(status().isCreated());
    }
    @Test void logoutInvalidaSessao() throws Exception {
        var session = login();
        mvc.perform(post("/api/auth/logout").session(session).with(csrf())).andExpect(status().isNoContent());
        assertThat(session.isInvalid()).isTrue();
        mvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());
    }
    @Test void logoutSemCsrfNaoEncerraSessao() throws Exception {
        var session = login();
        mvc.perform(post("/api/auth/logout").session(session)).andExpect(status().isForbidden());
        mvc.perform(get("/api/auth/me").session(session)).andExpect(status().isOk());
    }
    @Test void trocaDeSenhaInvalidaSessaoAnterior() throws Exception {
        var session = login();
        admin.setSenhaHash(encoder.encode("OutraSenha123!")); usuarios.saveAndFlush(admin);
        mvc.perform(get("/api/auth/me").session(session)).andExpect(status().isUnauthorized());
    }
    @Test void exclusaoInvalidaSessaoAnterior() throws Exception {
        var session = login(); usuarios.delete(admin); usuarios.flush();
        mvc.perform(get("/api/auth/me").session(session)).andExpect(status().isUnauthorized());
    }
    @Test void mudancaDePapelInvalidaSessaoAnterior() throws Exception {
        var session = login(); admin.setAdministrador(false); usuarios.saveAndFlush(admin);
        mvc.perform(get("/api/auth/me").session(session)).andExpect(status().isUnauthorized());
    }
    @Test void bloqueiaExcluirUltimoAdministrador() throws Exception {
        mvc.perform(delete("/api/usuarios/" + admin.getId()).session(login()).with(csrf()))
                .andExpect(status().isConflict());
    }
    @Test void protegePapelEContaDoAdministradorOriginalMesmoComOutroAdmin() throws Exception {
        admin.setAdministradorOriginal(true);
        usuarios.saveAndFlush(admin);
        var outroAdmin = cadastrarConta("outro-admin@example.invalid", true);
        var session = login();
        String payload = json.writeValueAsString(Map.of(
                "nome", admin.getNome(),
                "email", admin.getEmail(),
                "administrador", false));
        mvc.perform(put("/api/usuarios/" + admin.getId()).session(session).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isForbidden());
        var outraSessao = login(outroAdmin.getEmail(), SENHA);
        mvc.perform(delete("/api/usuarios/" + admin.getId()).session(outraSessao).with(csrf()))
                .andExpect(status().isForbidden());
        mvc.perform(put("/api/usuarios/" + admin.getId()).session(outraSessao).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Alterado\",\"email\":\"admin-auth@example.invalid\",\"senha\":\"NovaSenha123!\"}"))
                .andExpect(status().isForbidden());
        mvc.perform(put("/api/usuarios/" + admin.getId()).session(session).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Novo nome\",\"email\":\"original-novo@example.invalid\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.administradorOriginal").value(true));
        mvc.perform(get("/api/auth/me").session(login("original-novo@example.invalid", SENHA)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.administradorOriginal").value(true));
    }

    @Test void somenteAdministradorOriginalAtribuiOuAlteraPapeis() throws Exception {
        admin.setAdministradorOriginal(true);
        usuarios.saveAndFlush(admin);
        var outroAdmin = cadastrarConta("outro-admin@example.invalid", true);
        var design = cadastrarConta("design-papel@example.invalid", false);
        var outraSessao = login(outroAdmin.getEmail(), SENHA);
        String payload = json.writeValueAsString(Map.of(
                "nome", design.getNome(), "email", design.getEmail(), "administrador", true));
        var rota = "/api/usuarios/" + design.getId();

        mvc.perform(put(rota).session(outraSessao).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/usuarios").session(outraSessao).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Novo\",\"email\":\"novo@example.invalid\",\"senha\":\"TesteSeguro123!\",\"administrador\":true}"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/usuarios").session(outraSessao).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Novo\",\"email\":\"novo@example.invalid\",\"senha\":\"TesteSeguro123!\"}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.administrador").value(false));
        mvc.perform(put(rota).session(outraSessao).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Nome editado\",\"email\":\"design-papel@example.invalid\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.administrador").value(false));
        mvc.perform(put(rota).session(login()).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isOk()).andExpect(jsonPath("$.administrador").value(true));
    }

    private Usuario cadastrarConta(String email, boolean administrador) {
        var usuario = new Usuario();
        usuario.setNome("Conta de teste");
        usuario.setEmail(email);
        usuario.setSenhaHash(encoder.encode(SENHA));
        usuario.setAdministrador(administrador);
        return usuarios.saveAndFlush(usuario);
    }
    @Test void senhaUnicodeInvalidaRetorna400() throws Exception {
        var session = login();
        String payload = json.writeValueAsString(Map.of("nome", "Teste", "email", "unicode@example.invalid", "senha", "á".repeat(40)));
        mvc.perform(post("/api/usuarios").session(session).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(payload)).andExpect(status().isBadRequest());
        mvc.perform(put("/api/usuarios/" + admin.getId()).session(session).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(payload)).andExpect(status().isBadRequest());
        mvc.perform(post("/api/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(dados(EMAIL, "á".repeat(40)))).andExpect(status().isBadRequest());
    }
    @Test void adminCadastraOutroAdminSemExporHash() throws Exception {
        admin.setAdministradorOriginal(true);
        usuarios.saveAndFlush(admin);
        var session = login();
        String payload = json.writeValueAsString(Map.of("nome", "Segundo", "email", "segundo@example.invalid", "senha", SENHA));
        mvc.perform(post("/api/usuarios").session(session).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(payload)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.administrador").value(true))
                .andExpect(jsonPath("$.administradorOriginal").value(false))
                .andExpect(jsonPath("$.senhaHash").doesNotExist());
        var salvo = usuarios.findByEmail("segundo@example.invalid").orElseThrow();
        assertThat(salvo.isAdministrador()).isTrue();
        assertThat(encoder.matches(SENHA, salvo.getSenhaHash())).isTrue();
        mvc.perform(post("/api/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(dados(salvo.getEmail(), SENHA))).andExpect(status().isOk());
    }

    @Test void adminCadastraDesignEProtegeGestaoDeUsuarios() throws Exception {
        var sessionAdmin = login();
        String emailDesign = "design@example.invalid";
        String payload = json.writeValueAsString(Map.of(
                "nome", "Pessoa Design",
                "email", emailDesign,
                "senha", SENHA,
                "administrador", false));

        mvc.perform(post("/api/usuarios").session(sessionAdmin).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.administrador").value(false));

        var sessionDesign = login(emailDesign, SENHA);
        mvc.perform(get("/api/auth/me").session(sessionDesign))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.administrador").value(false));
        mvc.perform(get("/api/usuarios").session(sessionDesign))
                .andExpect(status().isForbidden());
    }
}
