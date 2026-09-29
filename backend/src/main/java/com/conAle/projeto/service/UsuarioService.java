package com.conAle.projeto.service;

import com.conAle.projeto.dto.UsuarioAtualizacaoRequest;
import com.conAle.projeto.dto.UsuarioRequest;
import com.conAle.projeto.dto.UsuarioResponse;
import com.conAle.projeto.model.Usuario;
import com.conAle.projeto.repository.UsuarioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

// Coordena as operações de usuários usando o repository.
// Por padrão, as transações são de leitura; os métodos de escrita sobrescrevem isso.
@Service
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    // O Spring fornece o repository e o codificador de senhas pelo construtor.
    public UsuarioService(
            UsuarioRepository repository,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // Ordena por ID decrescente e converte cada entidade em uma resposta.
    public List<UsuarioResponse> listar() {
        return repository.findAll(Sort.by("id").descending())
                .stream()
                .map(UsuarioResponse::from)
                .toList();
    }

    public UsuarioResponse buscar(Integer id) {
        return UsuarioResponse.from(encontrar(id));
    }

    // A transação confirma a gravação ao concluir ou a desfaz em caso de falha.
    @Transactional
    public UsuarioResponse criar(UsuarioRequest request) {
        String email = normalizarEmail(request.email());

        if (repository.existsByEmail(email)) {
            throw emailJaCadastrado();
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome().trim());
        usuario.setEmail(email);
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));

        Usuario salvo = repository.save(usuario);

        return UsuarioResponse.from(salvo);
    }

    // Busca o registro existente para manter seu ID na edição.
    // Senha omitida no request chega como null e mantém o hash anterior.
    @Transactional
    public UsuarioResponse atualizar(Integer id, UsuarioAtualizacaoRequest request) {
        Usuario usuario = encontrar(id);
        String email = normalizarEmail(request.email());

        if (repository.existsByEmailAndIdNot(email, id)) {
            throw emailJaCadastrado();
        }

        usuario.setNome(request.nome().trim());
        usuario.setEmail(email);

        if (request.senha() != null) {
            usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        }

        Usuario salvo = repository.save(usuario);

        return UsuarioResponse.from(salvo);
    }

    // Confere se o usuário existe antes de excluir definitivamente.
    @Transactional
    public void excluir(Integer id) {
        Usuario usuario = encontrar(id);
        repository.delete(usuario);
    }

    // Reutiliza a busca e retorna 404 quando o ID não existe.
    private Usuario encontrar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado."
                ));
    }

    // Remove espaços e usa minúsculas para que "A@x.com" e "a@x.com" sejam o mesmo e-mail.
    private String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    // O ApiExceptionHandler já converte este erro em resposta 409 com a mensagem.
    private ResponseStatusException emailJaCadastrado() {
        return new ResponseStatusException(
                HttpStatus.CONFLICT,
                "E-mail já cadastrado."
        );
    }
}