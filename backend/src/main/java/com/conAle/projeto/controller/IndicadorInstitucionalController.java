package com.conAle.projeto.Controller;

import com.conAle.projeto.dto.IndicadorInstitucionalRequest;
import com.conAle.projeto.dto.IndicadorInstitucionalResponse;
import com.conAle.projeto.service.IndicadorInstitucionalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

// Recebe as chamadas HTTP e devolve os resultados em JSON.
// As operações com o banco ficam no service.
@RestController
@RequestMapping("/api/indicadores-institucionais")
public class IndicadorInstitucionalController {

    private final IndicadorInstitucionalService service;

    // O Spring fornece o service ao criar este controller.
    public IndicadorInstitucionalController(IndicadorInstitucionalService service) {
        this.service = service;
    }

    // POST cadastra um indicador. @Valid confere as regras do DTO de entrada.
    @PostMapping
    public ResponseEntity<IndicadorInstitucionalResponse> criar(
            @Valid @RequestBody IndicadorInstitucionalRequest request
    ) {
        IndicadorInstitucionalResponse indicador = service.criar(request);
        URI localizacao = URI.create("/api/indicadores-institucionais/" + indicador.id());

        // Retorna 201 e o endereço do indicador no cabeçalho Location.
        return ResponseEntity.created(localizacao).body(indicador);
    }

    // GET sem ID lista os indicadores cadastrados.
    @GetMapping
    public List<IndicadorInstitucionalResponse> listar() {
        return service.listar();
    }

    // O ID vem do endereço, por exemplo: /api/indicadores-institucionais/1.
    @GetMapping("/{id}")
    public IndicadorInstitucionalResponse buscar(@PathVariable Integer id) {
        return service.buscar(id);
    }

    // PUT substitui todos os campos do indicador.
    @PutMapping("/{id}")
    public IndicadorInstitucionalResponse atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody IndicadorInstitucionalRequest request
    ) {
        return service.atualizar(id, request);
    }

    // DELETE exclui o indicador e retorna 204, sem corpo na resposta.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}