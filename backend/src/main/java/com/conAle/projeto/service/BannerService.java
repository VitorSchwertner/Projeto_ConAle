package com.conAle.projeto.service;

import com.conAle.projeto.Model.Banner;
import com.conAle.projeto.dto.BannerRequest;
import com.conAle.projeto.dto.BannerResponse;
import com.conAle.projeto.repository.BannerRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Coordena as operações de banners usando o repository.
// Por padrão, as transações são de leitura; os métodos de escrita sobrescrevem isso.
@Service
@Transactional(readOnly = true)
public class BannerService {

    private final BannerRepository repository;

    // O Spring fornece o repository pelo construtor.
    public BannerService(BannerRepository repository) {
        this.repository = repository;
    }

    // Ordena por ID decrescente e converte cada entidade em uma resposta.
    public List<BannerResponse> listar() {
        return repository.findAll(Sort.by("id").descending())
                .stream()
                .map(BannerResponse::from)
                .toList();
    }

    // Lista só os banners ativos, para o site público.
    public List<BannerResponse> listarAtivos() {
        return repository.findByAtivoTrue(Sort.by("id").descending())
                .stream()
                .map(BannerResponse::from)
                .toList();
    }

    public BannerResponse buscar(Integer id) {
        return BannerResponse.from(encontrar(id));
    }

    // A transação confirma a gravação ao concluir ou a desfaz em caso de falha.
    @Transactional
    public BannerResponse criar(BannerRequest request) {
        Banner banner = new Banner();
        preencher(banner, request);

        Banner salvo = repository.save(banner);

        return BannerResponse.from(salvo);
    }

    // Busca o registro existente para manter seu ID na edição.
    @Transactional
    public BannerResponse atualizar(Integer id, BannerRequest request) {
        Banner banner = encontrar(id);
        preencher(banner, request);

        Banner salvo = repository.save(banner);

        return BannerResponse.from(salvo);
    }

    // Confere se o banner existe antes de excluir definitivamente.
    @Transactional
    public void excluir(Integer id) {
        Banner banner = encontrar(id);
        repository.delete(banner);
    }

    // Reutiliza a busca e retorna 404 quando o ID não existe.
    private Banner encontrar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Banner não encontrado."
                ));
    }

    // Copia os dados do request para a entidade, aplicando a limpeza de espaços.
    private void preencher(Banner banner, BannerRequest request) {
        banner.setTitulo(request.titulo().trim());
        banner.setImagem(request.imagem().trim());
        banner.setLink(vazioParaNull(request.link()));
        // Se não vier no request, o banner nasce ativo.
        banner.setAtivo(request.ativo() == null || request.ativo());
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