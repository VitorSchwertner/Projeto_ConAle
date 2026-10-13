package com.conAle.projeto.service;

import com.conAle.projeto.Model.Rodape;
import com.conAle.projeto.dto.RodapeRequest;
import com.conAle.projeto.dto.RodapeResponse;
import com.conAle.projeto.repository.RodapeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

// Coordena as operações de rodapé usando o repository.
// Por padrão, as transações são de leitura; os métodos de escrita sobrescrevem isso.
@Service
@Transactional(readOnly = true)
public class RodapeService {

    private final RodapeRepository repository;

    // O Spring fornece o repository pelo construtor.
    public RodapeService(RodapeRepository repository) {
        this.repository = repository;
    }

    // Ordena por ID decrescente e converte cada entidade em uma resposta.
    public List<RodapeResponse> listar() {
        return repository.findAll(Sort.by("id").descending())
                .stream()
                .map(RodapeResponse::from)
                .toList();
    }

    public RodapeResponse buscar(Integer id) {
        return RodapeResponse.from(encontrar(id));
    }

    // A transação confirma a gravação ao concluir ou a desfaz em caso de falha.
    @Transactional
    public RodapeResponse criar(RodapeRequest request) {
        Rodape rodape = new Rodape();
        preencher(rodape, request);

        Rodape salvo = repository.save(rodape);

        return RodapeResponse.from(salvo);
    }

    // Busca o registro existente para manter seu ID na edição.
    @Transactional
    public RodapeResponse atualizar(Integer id, RodapeRequest request) {
        Rodape rodape = encontrar(id);
        preencher(rodape, request);

        Rodape salvo = repository.save(rodape);

        return RodapeResponse.from(salvo);
    }

    // Confere se o rodapé existe antes de excluir definitivamente.
    @Transactional
    public void excluir(Integer id) {
        Rodape rodape = encontrar(id);
        repository.delete(rodape);
    }

    // Reutiliza a busca e retorna 404 quando o ID não existe.
    private Rodape encontrar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Rodapé não encontrado."
                ));
    }

    // Copia os dados do request para a entidade, aplicando a limpeza de espaços.
    private void preencher(Rodape rodape, RodapeRequest request) {
        rodape.setTelefone(request.telefone().trim());
        rodape.setInstagram(vazioParaNull(request.instagram()));
        rodape.setEmail(request.email().trim().toLowerCase(Locale.ROOT));
        rodape.setDiasSemana(request.diasSemana().trim());
        rodape.setHorasTrabalhadas(request.horasTrabalhadas().trim());
        rodape.setEndereco(request.endereco().trim());
    }

    // Campo opcional: remove espaços e grava null quando ficar vazio.
    private String vazioParaNull(String valor) {
        if (valor == null) {
            return null;
        }
        String limpo = valor.trim();
        return limpo.isEmpty() ? null : limpo;
    }
}