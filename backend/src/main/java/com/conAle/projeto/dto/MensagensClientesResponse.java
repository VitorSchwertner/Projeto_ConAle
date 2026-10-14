package com.conAle.projeto.dto;

import com.conAle.projeto.Model.MensagensClientes;

// Define os campos devolvidos pela API.
public record MensagensClientesResponse(
        Integer id,
        String titulo,
        String areaAtuacao,
        Double nota,
        String mensagem
) {
    // Converte a entidade do banco no formato de resposta da API.
    public static MensagensClientesResponse from(MensagensClientes mensagemCliente) {
        return new MensagensClientesResponse(
                mensagemCliente.getId(),
                mensagemCliente.getTitulo(),
                mensagemCliente.getAreaAtuacao(),
                mensagemCliente.getNota(),
                mensagemCliente.getMensagem()
        );
    }
}