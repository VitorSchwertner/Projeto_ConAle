package com.conAle.projeto.service;

import com.conAle.projeto.Model.MensagensClientes;
import com.conAle.projeto.dto.MensagensClientesRequest;
import com.conAle.projeto.dto.MensagensClientesResponse;
import com.conAle.projeto.repository.MensagensClientesRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Coordena as operações de mensagens de clientes usando o repository.
// Por padrão, as transações são de leitura; os métodos de escrita sobrescrevem isso.
@Service
@Transactional(readOnly = true)
public class MensagensClientesService {

    private final MensagensClientesRepository repository;

    // O Spring fornece o repository pelo construtor.
    public MensagensClientesService(MensagensClientesRepository repository) {
        this.repository = repository;
    }

    // Ordena por ID decrescente e converte cada entidade em uma resposta.
    public List<MensagensClientesResponse> listar() {
        return repository.findAll(Sort.by("id").descending())
                .stream()
                .map(MensagensClientesResponse::from)
                .toList();
    }

    public MensagensClientesResponse buscar(Integer id) {
        return MensagensClientesResponse.from(encontrar(id));
    }

    // A transação confirma a gravação ao concluir ou a desfaz em caso de falha.
    @Transactional
    public MensagensClientesResponse criar(MensagensClientesRequest request) {
        MensagensClientes mensagemCliente = new MensagensClientes();
        preencher(mensagemCliente, request);

        MensagensClientes salvo = repository.save(mensagemCliente);

        return MensagensClientesResponse.from(salvo);
    }

    // Busca o registro existente para manter seu ID na edição.
    @Transactional
    public MensagensClientesResponse atualizar(Integer id, MensagensClientesRequest request) {
        MensagensClientes mensagemCliente = encontrar(id);
        preencher(mensagemCliente, request);

        MensagensClientes salvo = repository.save(mensagemCliente);

        return MensagensClientesResponse.from(salvo);
    }

    // Confere se a mensagem existe antes de excluir definitivamente.
    @Transactional
    public void excluir(Integer id) {
        MensagensClientes mensagemCliente = encontrar(id);
        repository.delete(mensagemCliente);
    }

    // Reutiliza a busca e retorna 404 quando o ID não existe.
    private MensagensClientes encontrar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Mensagem de cliente não encontrada."
                ));
    }

    // Copia os dados do request para a entidade, aplicando a limpeza de espaços.
    private void preencher(MensagensClientes mensagemCliente, MensagensClientesRequest request) {
        mensagemCliente.setTitulo(request.titulo().trim());
        mensagemCliente.setAreaAtuacao(request.areaAtuacao().trim());
        mensagemCliente.setNota(request.nota());
        mensagemCliente.setMensagem(request.mensagem().trim());
    }
}