package com.conAle.projeto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Dados recebidos no cadastro e na edição. O ID é gerado pelo banco.
// Se "ativo" for omitido (null), o service assume true.
public record BannerRequest(
        @NotBlank(message = "O título é obrigatório.")
        @Size(max = 150, message = "O título aceita até 150 caracteres.")
        String titulo,

        @NotBlank(message = "A imagem é obrigatória.")
        @Size(max = 500, message = "A imagem aceita até 500 caracteres.")
        String imagem,

        // Opcional: se omitido ou em branco, é gravado como null.
        @Size(max = 500, message = "O link aceita até 500 caracteres.")
        String link,

        @NotBlank(message = "A posição é obrigatória.")
        @Pattern(
                regexp = "HOME_TOPO|SERVICOS_TOPO|SOBRE_NOS|AREA_CLIENTE|SOBRE_MIM_FOTO",
                message = "A posição informada é inválida."
        )
        String posicao,

        Boolean ativo
) {
}
