package com.conAle.projeto.service;

import com.conAle.projeto.Model.Rodape;
import com.conAle.projeto.Model.RodapeAtivo;
import com.conAle.projeto.dto.RodapeModeloRequest;
import com.conAle.projeto.dto.RodapeModeloResponse;
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

    public RodapeResponse buscar() {
        return RodapeResponse.from(repository.buscarAtivo().orElseThrow(this::naoEncontrado));
    }

    // Busca o registro existente para manter seu ID na edição.
    @Transactional
    public RodapeResponse atualizar(RodapeRequest request) {
        Rodape rodape = encontrar(bloquearSelecao().getRodapeId());
        preencher(rodape, request);

        Rodape salvo = repository.save(rodape);

        return RodapeResponse.from(salvo);
    }

    public List<RodapeModeloResponse> listar() {
        Integer idAtivo = buscar().id();
        return repository.findAll(Sort.by("id")).stream()
                .map(rodape -> RodapeModeloResponse.from(rodape, idAtivo))
                .toList();
    }

    @Transactional
    public RodapeModeloResponse criar(RodapeModeloRequest request) {
        Rodape rodape = new Rodape();
        rodape.setNome(request.nome().trim());
        preencher(rodape, request.dados());
        return RodapeModeloResponse.from(repository.save(rodape), null);
    }

    @Transactional
    public RodapeModeloResponse atualizar(Integer id, RodapeModeloRequest request) {
        Integer idAtivo = bloquearSelecao().getRodapeId();
        Rodape rodape = encontrar(id);
        rodape.setNome(request.nome().trim());
        preencher(rodape, request.dados());
        return RodapeModeloResponse.from(rodape, idAtivo);
    }

    @Transactional
    public void ativar(Integer id) {
        RodapeAtivo selecao = bloquearSelecao();
        selecao.setRodapeId(encontrar(id).getId());
    }

    @Transactional
    public void excluir(Integer id) {
        if (bloquearSelecao().getRodapeId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ative outro rodapé antes de excluir este.");
        }
        repository.delete(encontrar(id));
    }

    private RodapeAtivo bloquearSelecao() {
        return repository.bloquearSelecao().orElseThrow(this::naoEncontrado);
    }

    private Rodape encontrar(Integer id) {
        return repository.findById(id).orElseThrow(this::naoEncontrado);
    }

    private ResponseStatusException naoEncontrado() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Rodapé não encontrado.");
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
