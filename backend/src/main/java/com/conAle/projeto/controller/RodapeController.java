package com.conAle.projeto.Controller;

import com.conAle.projeto.dto.RodapeModeloRequest;
import com.conAle.projeto.dto.RodapeModeloResponse;
import com.conAle.projeto.dto.RodapeRequest;
import com.conAle.projeto.dto.RodapeResponse;
import com.conAle.projeto.service.RodapeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Consulta o rodapé ativo no site público.
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

    @GetMapping("/modelos")
    public List<RodapeModeloResponse> listar() {
        return service.listar();
    }

    @PostMapping("/modelos")
    @ResponseStatus(HttpStatus.CREATED)
    public RodapeModeloResponse criar(@Valid @RequestBody RodapeModeloRequest request) {
        return service.criar(request);
    }

    @PutMapping("/modelos/{id}")
    public RodapeModeloResponse atualizar(@PathVariable Integer id, @Valid @RequestBody RodapeModeloRequest request) {
        return service.atualizar(id, request);
    }

    @PutMapping("/modelos/{id}/ativacao")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativar(@PathVariable Integer id) {
        service.ativar(id);
    }

    @DeleteMapping("/modelos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) {
        service.excluir(id);
    }
}
