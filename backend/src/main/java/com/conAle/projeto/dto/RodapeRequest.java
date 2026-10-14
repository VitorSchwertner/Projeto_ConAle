package com.conAle.projeto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Dados recebidos no cadastro e na edição. O ID é gerado pelo banco.
public record RodapeRequest(
        @NotBlank(message = "O telefone é obrigatório.")
        @Size(max = 20, message = "O telefone aceita até 20 caracteres.")
        String telefone,

        // Opcional: se omitido ou em branco, é gravado como null.
        @Size(max = 100, message = "O Instagram aceita até 100 caracteres.")
        String instagram,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Informe um e-mail válido.")
        @Size(max = 254, message = "O e-mail aceita até 254 caracteres.")
        String email,

        @NotBlank(message = "Os dias de funcionamento são obrigatórios.")
        @Size(max = 100, message = "Os dias de funcionamento aceitam até 100 caracteres.")
        String diasSemana,

        @NotBlank(message = "O horário de funcionamento é obrigatório.")
        @Size(max = 100, message = "O horário de funcionamento aceita até 100 caracteres.")
        String horasTrabalhadas,

        @NotBlank(message = "O endereço é obrigatório.")
        @Size(max = 255, message = "O endereço aceita até 255 caracteres.")
        String endereco
) {
}