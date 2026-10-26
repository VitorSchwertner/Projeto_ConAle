package com.conAle.projeto.security;

import com.conAle.projeto.Model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

// Fica apenas na sessão do servidor. Nunca é devolvido como resposta da API.
// O nome foi preservado para evitar uma migração ampla, mas representa os dois
// papéis internos: Administrador e Design.
public record AdminPrincipal(Integer id, String email, String senhaHash, boolean administrador)
        implements UserDetails {
    public static AdminPrincipal from(Usuario usuario) {
        return new AdminPrincipal(usuario.getId(), usuario.getEmail(), usuario.getSenhaHash(),
                usuario.isAdministrador());
    }
    public String getUsername() { return email; }
    public String getPassword() { return senhaHash; }
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(administrador ? "ROLE_ADMIN" : "ROLE_DESIGN"));
    }
}
