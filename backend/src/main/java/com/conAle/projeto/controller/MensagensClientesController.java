package com.conAle.projeto.Controller;

import com.conAle.projeto.dto.MensagensClientesRequest;
import com.conAle.projeto.dto.MensagensClientesResponse;
import com.conAle.projeto.service.MensagensClientesService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

// Recebe as chamadas HTTP e devolve os resultados em JSON.
// As operações com o banco ficam no service.
@RestController
@RequestMapping("/api/mensagens-clientes")
public class MensagensClientesController {

    private final MensagensClientesService service;

    // O Spring fornece o service ao criar este controller.
    public MensagensClientesController(MensagensClientesService service) {
        this.service = service;
    }

    // POST cadastra uma mensagem. @Valid confere as regras do DTO de entrada.
    @PostMapping
    public ResponseEntity<MensagensClientesResponse> criar(
            @Valid @RequestBody MensagensClientesRequest request
    ) {
        MensagensClientesResponse mensagemCliente = service.criar(request);
        URI localizacao = URI.create("/api/mensagens-clientes/" + mensagemCliente.id());

        // Retorna 201 e o endereço da mensagem no cabeçalho Location.
        return ResponseEntity.created(localizacao).body(mensagemCliente);
    }

    // GET sem ID lista as mensagens cadastradas.
    @GetMapping
    public List<MensagensClientesResponse> listar() {
        return service.listar();
    }

    // O ID vem do endereço, por exemplo: /api/mensagens-clientes/1.
    @GetMapping("/{id}")
    public MensagensClientesResponse buscar(@PathVariable Integer id) {
        return service.buscar(id);
    }

    // PUT substitui todos os campos da mensagem.
    @PutMapping("/{id}")
    public MensagensClientesResponse atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody MensagensClientesRequest request
    ) {
        return service.atualizar(id, request);
    }

    // DELETE exclui a mensagem e retorna 204, sem corpo na resposta.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}