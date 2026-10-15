package com.conAle.projeto.service;

import com.conAle.projeto.Model.Rodape;
import com.conAle.projeto.dto.RodapeRequest;
import com.conAle.projeto.dto.RodapeResponse;
import com.conAle.projeto.repository.RodapeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
        return RodapeResponse.from(encontrar());
    }

    // Busca o registro existente para manter seu ID na edição.
    @Transactional
    public RodapeResponse atualizar(RodapeRequest request) {
        Rodape rodape = encontrar();
        preencher(rodape, request);

        Rodape salvo = repository.save(rodape);

        return RodapeResponse.from(salvo);
    }

    // Sempre acessa o registro criado pela V1, nunca cria um substituto.
    private Rodape encontrar() {
        return repository.findById(1)
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
