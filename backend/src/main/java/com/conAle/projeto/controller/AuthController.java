package com.conAle.projeto.controller;

import com.conAle.projeto.dto.*;
import com.conAle.projeto.repository.UsuarioRepository;
import com.conAle.projeto.security.AdminPrincipal;
import jakarta.servlet.http.*;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager manager;
    private final SecurityContextRepository contexts;
    private final SessionAuthenticationStrategy sessions;
    private final UsuarioRepository usuarios;
    public AuthController(AuthenticationManager manager, SecurityContextRepository contexts,
            SessionAuthenticationStrategy sessions, UsuarioRepository usuarios) {
        this.manager = manager; this.contexts = contexts; this.sessions = sessions; this.usuarios = usuarios;
    }
    // O Postman obtém o token e mantém o cookie da mesma sessão.
    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken token) {
        return Map.of("headerName", token.getHeaderName(), "token", token.getToken());
    }
    @PostMapping("/login")
    public UsuarioResponse login(@Valid @RequestBody LoginRequest dados,
            HttpServletRequest request, HttpServletResponse response) {
        try {
            var auth = manager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(
                    dados.email(), dados.senha()));
            sessions.onAuthentication(auth, request, response);
            var context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            contexts.saveContext(context, request, response);
            return me((AdminPrincipal) auth.getPrincipal());
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos.");
        }
    }
    @GetMapping("/me")
    public UsuarioResponse me(@AuthenticationPrincipal AdminPrincipal admin) {
        return UsuarioResponse.from(usuarios.findById(admin.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED)));
    }
}
