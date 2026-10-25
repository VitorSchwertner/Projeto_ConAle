package com.conAle.projeto.dto;

import com.conAle.projeto.Model.Banner;

// Define os campos devolvidos pela API.
public record BannerResponse(
        Integer id,
        String titulo,
        String imagem,
        String link,
        String posicao,
        boolean ativo
) {
    // Converte a entidade do banco no formato de resposta da API.
    public static BannerResponse from(Banner banner) {
        return new BannerResponse(
                banner.getId(),
                banner.getTitulo(),
                banner.getImagem(),
                banner.getLink(),
                banner.getPosicao(),
                banner.isAtivo()
        );
    }
}
