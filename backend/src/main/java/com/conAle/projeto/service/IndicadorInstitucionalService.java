package com.conAle.projeto.service;

import com.conAle.projeto.Model.IndicadorInstitucional;
import com.conAle.projeto.dto.IndicadorInstitucionalRequest;
import com.conAle.projeto.dto.IndicadorInstitucionalResponse;
import com.conAle.projeto.repository.IndicadorInstitucionalRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Coordena as operações de indicadores institucionais usando o repository.
// Por padrão, as transações são de leitura; os métodos de escrita sobrescrevem isso.
@Service
@Transactional(readOnly = true)
public class IndicadorInstitucionalService {

    private final IndicadorInstitucionalRepository repository;

    // O Spring fornece o repository pelo construtor.
    public IndicadorInstitucionalService(IndicadorInstitucionalRepository repository) {
        this.repository = repository;
    }

    // Ordena por ID decrescente e converte cada entidade em uma resposta.
    public List<IndicadorInstitucionalResponse> listar() {
        return repository.findAll(Sort.by("id").descending())
                .stream()
                .map(IndicadorInstitucionalResponse::from)
                .toList();
    }

    public IndicadorInstitucionalResponse buscar(Integer id) {
        return IndicadorInstitucionalResponse.from(encontrar(id));
    }

    // A transação confirma a gravação ao concluir ou a desfaz em caso de falha.
    @Transactional
    public IndicadorInstitucionalResponse criar(IndicadorInstitucionalRequest request) {
        IndicadorInstitucional indicador = new IndicadorInstitucional();
        preencher(indicador, request);

        IndicadorInstitucional salvo = repository.save(indicador);

        return IndicadorInstitucionalResponse.from(salvo);
    }

    // Busca o registro existente para manter seu ID na edição.
    @Transactional
    public IndicadorInstitucionalResponse atualizar(Integer id, IndicadorInstitucionalRequest request) {
        IndicadorInstitucional indicador = encontrar(id);
        preencher(indicador, request);

        IndicadorInstitucional salvo = repository.save(indicador);

        return IndicadorInstitucionalResponse.from(salvo);
    }

    // Confere se o indicador existe antes de excluir definitivamente.
    @Transactional
    public void excluir(Integer id) {
        IndicadorInstitucional indicador = encontrar(id);
        repository.delete(indicador);
    }

    // Reutiliza a busca e retorna 404 quando o ID não existe.
    private IndicadorInstitucional encontrar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Indicador institucional não encontrado."
                ));
    }

    // Copia os dados do request para a entidade.
    private void preencher(IndicadorInstitucional indicador, IndicadorInstitucionalRequest request) {
        indicador.setAnosAtuacao(request.anosAtuacao());
        indicador.setEmpresasAbertas(request.empresasAbertas());
    }
}