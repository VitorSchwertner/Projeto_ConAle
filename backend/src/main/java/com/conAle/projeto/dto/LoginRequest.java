package com.conAle.projeto.dto;

import com.conAle.projeto.validation.SenhaBcrypt;
import jakarta.validation.constraints.*;

public record LoginRequest(
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Size(max = 72) @SenhaBcrypt String senha
) { }
