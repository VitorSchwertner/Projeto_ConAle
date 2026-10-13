package com.conAle.projeto.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// Dados recebidos no cadastro e na edição. O ID é gerado pelo banco.
// Usa Integer (e não int) para que um campo ausente seja detectado pelo @NotNull.
public record IndicadorInstitucionalRequest(
        @NotNull(message = "Os anos de atuação são obrigatórios.")
        @Min(value = 0, message = "Os anos de atuação não podem ser negativos.")
        Integer anosAtuacao,

        @NotNull(message = "O número de empresas abertas é obrigatório.")
        @Min(value = 0, message = "O número de empresas abertas não pode ser negativo.")
        Integer empresasAbertas
) {
}