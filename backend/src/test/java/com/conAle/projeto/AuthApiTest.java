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
        mvc.perform(get("/api/rodape")).andExpect(status().isOk());
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
                        + "\"posicao\":\"HOME_TOPO\",\"ativo\":true}"))
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
    @Test void bloqueiaTransformarUltimoAdministradorEmDesign() throws Exception {
        var session = login();
        String payload = json.writeValueAsString(Map.of(
                "nome", admin.getNome(),
                "email", admin.getEmail(),
                "administrador", false));
        mvc.perform(put("/api/usuarios/" + admin.getId()).session(session).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isConflict());
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
        var session = login();
        String payload = json.writeValueAsString(Map.of("nome", "Segundo", "email", "segundo@example.invalid", "senha", SENHA));
        mvc.perform(post("/api/usuarios").session(session).with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(payload)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.administrador").value(true))
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
