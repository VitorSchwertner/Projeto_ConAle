package com.conAle.projeto.dto;

import com.conAle.projeto.Model.Usuario;

// Define os campos devolvidos pela API. Não inclui senha nem hash.
public record UsuarioResponse(
        Integer id,
        String nome,
        String email,
        boolean administrador
) {
    // Converte a entidade do banco no formato de resposta da API.
    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.isAdministrador()
        );
    }
}
