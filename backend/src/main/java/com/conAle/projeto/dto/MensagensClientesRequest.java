package com.conAle.projeto.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Dados recebidos no cadastro e na edição. O ID é gerado pelo banco.
public record MensagensClientesRequest(
        @NotBlank(message = "O título é obrigatório.")
        @Size(max = 150, message = "O título aceita até 150 caracteres.")
        String titulo,

        @NotBlank(message = "A área de atuação é obrigatória.")
        @Size(max = 150, message = "A área de atuação aceita até 150 caracteres.")
        String areaAtuacao,

        @NotNull(message = "A nota é obrigatória.")
        @DecimalMin(value = "0.0", message = "A nota não pode ser menor que 0.")
        @DecimalMax(value = "5.0", message = "A nota não pode ser maior que 5.")
        Double nota,

        @NotBlank(message = "A mensagem é obrigatória.")
        @Size(max = 1000, message = "A mensagem aceita até 1000 caracteres.")
        String mensagem
) {
}