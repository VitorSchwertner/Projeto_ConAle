package com.conAle.projeto.controller;

import com.conAle.projeto.dto.UsuarioAtualizacaoRequest;
import com.conAle.projeto.dto.UsuarioRequest;
import com.conAle.projeto.dto.UsuarioResponse;
import com.conAle.projeto.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

// Recebe as chamadas HTTP e devolve os resultados em JSON.
// As operações com o banco ficam no service.
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    // O Spring fornece o service ao criar este controller.
    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // POST cadastra um usuário. @Valid confere as regras do DTO de entrada.
    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(
            @Valid @RequestBody UsuarioRequest request
    ) {
        UsuarioResponse usuario = service.criar(request);
        URI localizacao = URI.create("/api/usuarios/" + usuario.id());

        // Retorna 201 e o endereço do usuário no cabeçalho Location.
        return ResponseEntity.created(localizacao).body(usuario);
    }

    // GET sem ID lista os usuários cadastrados.
    @GetMapping
    public List<UsuarioResponse> listar() {
        return service.listar();
    }

    // O ID vem do endereço, por exemplo: /api/usuarios/1.
    @GetMapping("/{id}")
    public UsuarioResponse buscar(@PathVariable Integer id) {
        return service.buscar(id);
    }

    // PUT substitui nome e e-mail; a senha só muda se for enviada.
    @PutMapping("/{id}")
    public UsuarioResponse atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioAtualizacaoRequest request
    ) {
        return service.atualizar(id, request);
    }

    // DELETE exclui o usuário e retorna 204, sem corpo na resposta.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
