package com.conAle.projeto.dto;

import com.conAle.projeto.model.Noticia;
import com.fasterxml.jackson.annotation.JsonProperty;

// Define os campos devolvidos pela API, incluindo o ID gerado no cadastro.
public record NoticiaResponse(
        Integer id,
        String titulo,
        @JsonProperty("sub_titulo") String subTitulo
) {
    // Converte a entidade do banco no formato de resposta da API.
    public static NoticiaResponse from(Noticia noticia) {
        return new NoticiaResponse(
                noticia.getId(),
                noticia.getTitulo(),
                noticia.getSubTitulo()
        );
    }
}
