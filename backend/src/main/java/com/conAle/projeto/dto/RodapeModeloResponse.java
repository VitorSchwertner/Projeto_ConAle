package com.conAle.projeto.dto;

import com.conAle.projeto.Model.Rodape;

public record RodapeModeloResponse(String nome, boolean ativo, RodapeResponse dados) {
    public static RodapeModeloResponse from(Rodape rodape, Integer idAtivo) {
        return new RodapeModeloResponse(rodape.getNome(), rodape.getId().equals(idAtivo), RodapeResponse.from(rodape));
    }
}
