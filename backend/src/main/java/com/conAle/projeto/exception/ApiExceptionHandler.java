package com.conAle.projeto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

// Centraliza as respostas de erro para os controllers da API.
@RestControllerAdvice
public class ApiExceptionHandler {

    // Quando @Valid rejeita os dados, devolve 400 e uma mensagem por campo.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validar(MethodArgumentNotValidException exception) {
        Map<String, String> campos = new LinkedHashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(erro -> {
            // Usa o mesmo nome de campo que o cliente envia no JSON.
            String campo = erro.getField().equals("subTitulo")
                    ? "sub_titulo"
                    : erro.getField();

            campos.putIfAbsent(campo, erro.getDefaultMessage());
        });

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Confira os campos enviados."
        );

        problema.setProperty("campos", campos);
        return problema;
    }

    // Preserva o status e a mensagem definidos pelo service, como o erro 404.
    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail tratarStatus(ResponseStatusException exception) {
        return ProblemDetail.forStatusAndDetail(
                exception.getStatusCode(),
                exception.getReason()
        );
    }
}
