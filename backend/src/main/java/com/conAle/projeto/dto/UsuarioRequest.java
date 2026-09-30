package com.conAle.projeto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Dados recebidos no cadastro. O ID é gerado pelo banco.
// A senha chega em texto puro e vira hash no service; ela nunca é devolvida.
public record UsuarioRequest(
        // Não permite nome ausente, vazio ou formado apenas por espaços.
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 150, message = "O nome aceita até 150 caracteres.")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um e-mail válido.")
        @Size(max = 254, message = "O e-mail aceita até 254 caracteres.")
        String email,

        // Valida caracteres e também o limite em bytes do BCrypt.
        @com.conAle.projeto.validation.SenhaBcrypt
        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 8, max = 72, message = "A senha deve ter de 8 a 72 caracteres.")
        String senha
) {
}
