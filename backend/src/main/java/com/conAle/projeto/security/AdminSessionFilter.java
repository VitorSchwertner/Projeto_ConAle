package com.conAle.projeto.security;

import com.conAle.projeto.repository.UsuarioRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// Exclusão, revogação e troca de senha/e-mail invalidam as sessões anteriores.
public class AdminSessionFilter extends OncePerRequestFilter {
    private final UsuarioRepository repository;
    public AdminSessionFilter(UsuarioRepository repository) { this.repository = repository; }
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain chain) throws ServletException, IOException {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AdminPrincipal principal) {
            boolean valida = repository.findById(principal.id())
                    .filter(u -> u.isAdministrador()
                            && u.getSenhaHash().equals(principal.senhaHash())
                            && u.getEmail().equals(principal.email())).isPresent();
            if (!valida) {
                var session = request.getSession(false);
                if (session != null) session.invalidate();
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
