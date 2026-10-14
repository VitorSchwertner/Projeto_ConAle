package com.conAle.projeto.dto;

import com.conAle.projeto.Model.Rodape;

// Define os campos devolvidos pela API.
public record RodapeResponse(
        Integer id,
        String telefone,
        String instagram,
        String email,
        String diasSemana,
        String horasTrabalhadas,
        String endereco
) {
    // Converte a entidade do banco no formato de resposta da API.
    public static RodapeResponse from(Rodape rodape) {
        return new RodapeResponse(
                rodape.getId(),
                rodape.getTelefone(),
                rodape.getInstagram(),
                rodape.getEmail(),
                rodape.getDiasSemana(),
                rodape.getHorasTrabalhadas(),
                rodape.getEndereco()
        );
    }
}