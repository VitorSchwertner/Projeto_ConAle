package com.conAle.projeto.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RodapeModeloRequest(
        @NotBlank @Size(max = 100) String nome,
        @NotNull @Valid RodapeRequest dados
) {}
