package com.conAle.projeto.Controller;

import com.conAle.projeto.dto.RodapeRequest;
import com.conAle.projeto.dto.RodapeResponse;
import com.conAle.projeto.service.RodapeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    // POST cadastra um rodapé. @Valid confere as regras do DTO de entrada.
    @PostMapping
    public ResponseEntity<RodapeResponse> criar(
            @Valid @RequestBody RodapeRequest request
    ) {
        RodapeResponse rodape = service.criar(request);
        URI localizacao = URI.create("/api/rodape/" + rodape.id());

        // Retorna 201 e o endereço do rodapé no cabeçalho Location.
        return ResponseEntity.created(localizacao).body(rodape);
    }

    // GET sem ID lista os rodapés cadastrados.
    @GetMapping
    public List<RodapeResponse> listar() {
        return service.listar();
    }

    // O ID vem do endereço, por exemplo: /api/rodape/1.
    @GetMapping("/{id}")
    public RodapeResponse buscar(@PathVariable Integer id) {
        return service.buscar(id);
    }

    // PUT substitui todos os campos do rodapé.
    @PutMapping("/{id}")
    public RodapeResponse atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody RodapeRequest request
    ) {
        return service.atualizar(id, request);
    }

    // DELETE exclui o rodapé e retorna 204, sem corpo na resposta.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}