package com.conAle.projeto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.nio.charset.StandardCharsets;

public class SenhaBcryptValidator implements ConstraintValidator<SenhaBcrypt, String> {
    public boolean isValid(String senha, ConstraintValidatorContext context) {
        // Null é permitido aqui porque a edição pode manter a senha anterior.
        return senha == null || senha.getBytes(StandardCharsets.UTF_8).length <= 72;
    }
}
