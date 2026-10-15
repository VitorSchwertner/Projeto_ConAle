package com.conAle.projeto.Controller;

import com.conAle.projeto.dto.RodapeRequest;
import com.conAle.projeto.dto.RodapeResponse;
import com.conAle.projeto.service.RodapeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

// Recebe as chamadas HTTP e devolve os resultados em JSON.
// As operações com o banco ficam no service.
@RestController
@RequestMapping("/api/rodape")
public class RodapeController {

    private final RodapeService service;

    // O Spring fornece o service ao criar este controller.
    public RodapeController(RodapeService service) {
        this.service = service;
    }

    // Consulta o cadastro unico, sempre com id = 1.
    @GetMapping
    public RodapeResponse buscar() {
        return service.buscar();
    }

    // PUT substitui todos os campos do rodapé.
    @PutMapping
    public RodapeResponse atualizar(
            @Valid @RequestBody RodapeRequest request
    ) {
        return service.atualizar(request);
    }
}
