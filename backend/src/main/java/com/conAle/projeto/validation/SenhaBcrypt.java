package com.conAle.projeto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

// BCrypt limita bytes UTF-8, que podem ser mais numerosos que os caracteres.
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SenhaBcryptValidator.class)
public @interface SenhaBcrypt {
    String message() default "A senha deve ocupar no máximo 72 bytes em UTF-8.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
