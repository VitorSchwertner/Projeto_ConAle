package com.conAle.projeto.Controller;

import com.conAle.projeto.dto.BannerRequest;
import com.conAle.projeto.dto.BannerResponse;
import com.conAle.projeto.service.BannerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

// Recebe as chamadas HTTP e devolve os resultados em JSON.
// As operações com o banco ficam no service.
@RestController
@RequestMapping("/api/banners")
public class BannerController {

    private final BannerService service;

    // O Spring fornece o service ao criar este controller.
    public BannerController(BannerService service) {
        this.service = service;
    }

    // POST cadastra um banner. @Valid confere as regras do DTO de entrada.
    @PostMapping
    public ResponseEntity<BannerResponse> criar(
            @Valid @RequestBody BannerRequest request
    ) {
        BannerResponse banner = service.criar(request);
        URI localizacao = URI.create("/api/banners/" + banner.id());

        // Retorna 201 e o endereço do banner no cabeçalho Location.
        return ResponseEntity.created(localizacao).body(banner);
    }

    // GET sem ID lista todos os banners (uso do painel administrativo).
    @GetMapping
    public List<BannerResponse> listar() {
        return service.listar();
    }

    // GET /api/banners/ativos lista só os banners exibidos no site.
    // Precisa vir declarado de forma que "ativos" não seja confundido com um {id}.
    @GetMapping("/ativos")
    public List<BannerResponse> listarAtivos() {
        return service.listarAtivos();
    }

    // O ID vem do endereço, por exemplo: /api/banners/1.
    @GetMapping("/{id}")
    public BannerResponse buscar(@PathVariable Integer id) {
        return service.buscar(id);
    }

    // PUT substitui todos os campos do banner.
    @PutMapping("/{id}")
    public BannerResponse atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody BannerRequest request
    ) {
        return service.atualizar(id, request);
    }

    // DELETE exclui o banner e retorna 204, sem corpo na resposta.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}