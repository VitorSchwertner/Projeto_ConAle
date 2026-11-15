package com.conAle.projeto.service;

import com.conAle.projeto.dto.UsuarioAtualizacaoRequest;
import com.conAle.projeto.dto.UsuarioRequest;
import com.conAle.projeto.dto.UsuarioResponse;
import com.conAle.projeto.Model.Usuario;
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
    public UsuarioResponse criar(UsuarioRequest request, Integer autorId) {
        boolean original = repository.existsByIdAndAdministradorOriginalTrue(autorId);
        if (Boolean.TRUE.equals(request.administrador()) && !original) {
            throw acessoAoPapelNegado();
        }
        boolean administrador = request.administrador() == null ? original : request.administrador();
        return salvarNovo(request, administrador, false);
    }

    @Transactional
    public void criarAdministradorOriginal(UsuarioRequest request) {
        salvarNovo(request, true, true);
    }

    private UsuarioResponse salvarNovo(UsuarioRequest request, boolean administrador, boolean original) {
        String email = normalizarEmail(request.email());

        if (repository.existsByEmail(email)) {
            throw emailJaCadastrado();
        }

        Usuario usuario = new Usuario();
        usuario.setAdministrador(administrador);
        usuario.setAdministradorOriginal(original);
        usuario.setNome(request.nome().trim());
        usuario.setEmail(email);
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));

        Usuario salvo = repository.save(usuario);

        return UsuarioResponse.from(salvo);
    }

    // Busca o registro existente para manter seu ID na edição.
    // Senha omitida no request chega como null e mantém o hash anterior.
    @Transactional
    public UsuarioResponse atualizar(Integer id, UsuarioAtualizacaoRequest request, Integer autorId) {
        Usuario usuario = encontrar(id);
        if (usuario.isAdministradorOriginal() && !id.equals(autorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Somente o administrador original pode editar os dados da própria conta.");
        }

        boolean alterandoPapel = request.administrador() != null
                && request.administrador() != usuario.isAdministrador();
        if (alterandoPapel) {
            if (usuario.isAdministradorOriginal()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "O papel do administrador original não pode ser alterado.");
            }
            if (!repository.existsByIdAndAdministradorOriginalTrue(autorId)) {
                throw acessoAoPapelNegado();
            }
            usuario.setAdministrador(request.administrador());
        }

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
        if (usuario.isAdministradorOriginal()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Não é permitido excluir o administrador original.");
        }
        if (usuario.isAdministrador() && repository.administradoresParaExclusao().size() <= 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é permitido excluir o último administrador.");
        }
        repository.delete(usuario);
    }

    private ResponseStatusException acessoAoPapelNegado() {
        return new ResponseStatusException(HttpStatus.FORBIDDEN,
                "Somente o administrador original pode atribuir o papel de Administrador ou alterar papéis.");
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
