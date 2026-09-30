package com.conAle.projeto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Dados recebidos na edição. Igual ao cadastro, exceto pela senha, que é opcional.
public record UsuarioAtualizacaoRequest(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 150, message = "O nome aceita até 150 caracteres.")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um e-mail válido.")
        @Size(max = 254, message = "O e-mail aceita até 254 caracteres.")
        String email,

        // Se for omitida (null), a senha atual é mantida. Se vier, é validada e trocada.
        @Size(min = 8, max = 72, message = "A senha deve ter de 8 a 72 caracteres.")
        String senha
) {
}