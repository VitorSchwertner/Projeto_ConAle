package com.conAle.projeto.security;

import com.conAle.projeto.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

// Fica apenas na sessão do servidor. Nunca é devolvido como resposta da API.
public record AdminPrincipal(Integer id, String email, String senhaHash) implements UserDetails {
    public static AdminPrincipal from(Usuario usuario) {
        return new AdminPrincipal(usuario.getId(), usuario.getEmail(), usuario.getSenhaHash());
    }
    public String getUsername() { return email; }
    public String getPassword() { return senhaHash; }
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
