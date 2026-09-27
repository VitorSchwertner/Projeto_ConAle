package com.conAle.projeto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Dados recebidos no cadastro e na edição. O ID é gerado pelo banco.
// O record fornece os acessos titulo() e subTitulo().
public record NoticiaRequest(
        // Não permite título ausente, vazio ou formado apenas por espaços.
        @NotBlank(message = "O título é obrigatório.")
        @Size(max = 200, message = "O título aceita até 200 caracteres.")
        String titulo,

        // No JSON usamos sub_titulo; no Java, subTitulo. O campo é opcional.
        @JsonProperty("sub_titulo")
        @Size(max = 500, message = "O subtítulo aceita até 500 caracteres.")
        String subTitulo
) {
}
