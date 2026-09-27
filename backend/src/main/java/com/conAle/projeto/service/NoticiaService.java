package com.conAle.projeto.service;

import com.conAle.projeto.dto.NoticiaRequest;
import com.conAle.projeto.dto.NoticiaResponse;
import com.conAle.projeto.model.Noticia;
import com.conAle.projeto.repository.NoticiaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Coordena as operações de notícias usando o repository.
// Por padrão, as transações são de leitura; os métodos de escrita sobrescrevem isso.
@Service
@Transactional(readOnly = true)
public class NoticiaService {

    private final NoticiaRepository repository;

    // O Spring fornece o repository pelo construtor.
    public NoticiaService(NoticiaRepository repository) {
        this.repository = repository;
    }

    // Ordena por ID decrescente e converte cada entidade em uma resposta.
    public List<NoticiaResponse> listar() {
        return repository.findAll(Sort.by("id").descending())
                .stream()
                .map(NoticiaResponse::from)
                .toList();
    }

    public NoticiaResponse buscar(Integer id) {
        return NoticiaResponse.from(encontrar(id));
    }

    // A transação confirma a gravação ao concluir ou a desfaz em caso de falha.
    @Transactional
    public NoticiaResponse criar(NoticiaRequest request) {
        Noticia noticia = new Noticia();
        noticia.setTitulo(request.titulo());
        noticia.setSubTitulo(request.subTitulo());

        Noticia salva = repository.save(noticia);

        return NoticiaResponse.from(salva);
    }

    // Busca o registro existente para manter seu ID na edição.
    // Subtítulo omitido no request chega como null e remove o valor anterior.
    @Transactional
    public NoticiaResponse atualizar(Integer id, NoticiaRequest request) {
        Noticia noticia = encontrar(id);

        noticia.setTitulo(request.titulo());
        noticia.setSubTitulo(request.subTitulo());

        Noticia salva = repository.save(noticia);

        return NoticiaResponse.from(salva);
    }

    // Confere se a notícia existe antes de excluir definitivamente.
    @Transactional
    public void excluir(Integer id) {
        Noticia noticia = encontrar(id);
        repository.delete(noticia);
    }

    // Reutiliza a busca e retorna 404 quando o ID não existe.
    private Noticia encontrar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Notícia não encontrada."
                ));
    }
}
