package com.conAle.projeto.dto;

import com.conAle.projeto.Model.IndicadorInstitucional;

// Define os campos devolvidos pela API.
public record IndicadorInstitucionalResponse(
        Integer id,
        int anosAtuacao,
        int empresasAbertas
) {
    // Converte a entidade do banco no formato de resposta da API.
    public static IndicadorInstitucionalResponse from(IndicadorInstitucional indicador) {
        return new IndicadorInstitucionalResponse(
                indicador.getId(),
                indicador.getAnosAtuacao(),
                indicador.getEmpresasAbertas()
        );
    }
}