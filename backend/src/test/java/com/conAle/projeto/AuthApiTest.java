package com.conAle.projeto;

import com.conAle.projeto.model.Usuario;
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
        return (MockHttpSession) mvc.perform(post("/api/auth/login").with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(dados(EMAIL, SENHA)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.senhaHash").doesNotExist())
                .andExpect(jsonPath("$.senha").doesNotExist())
                .andReturn().getRequest().getSession(false);
    }

    @Test void loginMantemSessao() throws Exception {
        var session = login();
        mvc.perform(get("/api/auth/me").session(session)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(admin.getId()));
        mvc.perform(get("/api/usuarios").session(session)).andExpect(status().isOk());
    }
    @Test void noticiasSaoPublicasUsuariosNao() throws Exception {
        mvc.perform(get("/api/noticias")).andExpect(status().isOk());
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
    @Test void contaAntigaSemPermissaoNaoEntra() throws Exception {
        admin.setAdministrador(false); usuarios.saveAndFlush(admin);
        mvc.perform(post("/api/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(dados(EMAIL, SENHA))).andExpect(status().isUnauthorized());
    }
    @Test void csrfObrigatorioInclusiveNoLogin() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(dados(EMAIL, SENHA))).andExpect(status().isForbidden());
        mvc.perform(post("/api/noticias").session(login()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Bloqueado\"}")).andExpect(status().isForbidden());
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
    @Test void revogacaoInvalidaSessaoAnterior() throws Exception {
        var session = login(); admin.setAdministrador(false); usuarios.saveAndFlush(admin);
        mvc.perform(get("/api/auth/me").session(session)).andExpect(status().isUnauthorized());
    }
    @Test void bloqueiaExcluirUltimoAdministrador() throws Exception {
        mvc.perform(delete("/api/usuarios/" + admin.getId()).session(login()).with(csrf()))
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
                .content(payload)).andExpect(status().isCreated()).andExpect(jsonPath("$.senhaHash").doesNotExist());
        var salvo = usuarios.findByEmail("segundo@example.invalid").orElseThrow();
        assertThat(salvo.isAdministrador()).isTrue();
        assertThat(encoder.matches(SENHA, salvo.getSenhaHash())).isTrue();
        mvc.perform(post("/api/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content(dados(salvo.getEmail(), SENHA))).andExpect(status().isOk());
    }
}
