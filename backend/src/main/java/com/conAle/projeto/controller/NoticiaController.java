package com.conAle.projeto.controller;

import com.conAle.projeto.dto.NoticiaRequest;
import com.conAle.projeto.dto.NoticiaResponse;
import com.conAle.projeto.service.NoticiaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

// Recebe as chamadas HTTP e devolve os resultados em JSON.
// As operações com o banco ficam no service.
@RestController
@RequestMapping("/api/noticias")
public class NoticiaController {

    private final NoticiaService service;

    // O Spring fornece o service ao criar este controller.
    public NoticiaController(NoticiaService service) {
        this.service = service;
    }

    // POST cadastra uma notícia. @Valid confere as regras do DTO de entrada.
    @PostMapping
    public ResponseEntity<NoticiaResponse> criar(
            @Valid @RequestBody NoticiaRequest request
    ) {
        NoticiaResponse noticia = service.criar(request);
        URI localizacao = URI.create("/api/noticias/" + noticia.id());

        // Retorna 201 e o endereço da notícia no cabeçalho Location.
        return ResponseEntity.created(localizacao).body(noticia);
    }

    // GET sem ID lista as notícias cadastradas.
    @GetMapping
    public List<NoticiaResponse> listar() {
        return service.listar();
    }

    // O ID vem do endereço, por exemplo: /api/noticias/1.
    @GetMapping("/{id}")
    public NoticiaResponse buscar(@PathVariable Integer id) {
        return service.buscar(id);
    }

    // PUT substitui o título e o subtítulo da notícia indicada.
    @PutMapping("/{id}")
    public NoticiaResponse atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody NoticiaRequest request
    ) {
        return service.atualizar(id, request);
    }

    // DELETE exclui a notícia e retorna 204, sem corpo na resposta.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
