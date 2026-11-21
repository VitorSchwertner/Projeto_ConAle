package com.conAle.projeto.service;

import com.conAle.projeto.Model.BannerFormato;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.UUID;

// Valida e grava as imagens enviadas pelo painel.
@Service
public class ArquivoService {

    private static final long TAMANHO_MAXIMO = 5L * 1024 * 1024;
    private static final double TOLERANCIA_PROPORCAO = 0.02; // 2%
    private static final double LARGURA_MINIMA = 0.5;        // 50% do tamanho final
    private static final byte[] ASSINATURA_PNG =
            {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};

    private final Path pastaBanners;

    public ArquivoService(@Value("${app.upload.dir}") String pastaUpload) {
        this.pastaBanners = Path.of(pastaUpload)
                .toAbsolutePath().normalize().resolve("banners");
    }

    // Salva a imagem e devolve o caminho público (ex.: /uploads/banners/uuid.jpg).
    public String salvarImagemBanner(MultipartFile arquivo, String posicao, boolean mobile) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw erro("Selecione uma imagem.");
        }
        if (arquivo.getSize() > TAMANHO_MAXIMO) {
            throw erro("A imagem deve ter no máximo 5 MB.");
        }

        BannerFormato formato = formato(posicao);
        if (mobile && !formato.temMobile()) {
            throw erro("Esta posição não tem versão mobile.");
        }

        byte[] bytes;
        try {
            bytes = arquivo.getBytes();
        } catch (IOException e) {
            throw erro("Não foi possível ler a imagem.");
        }

        String extensao = detectarExtensao(bytes);
        validarDimensoes(bytes, formato.largura(mobile), formato.altura(mobile));

        String nome = UUID.randomUUID() + "." + extensao;
        try {
            Files.createDirectories(pastaBanners);
            Files.write(pastaBanners.resolve(nome), bytes);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível salvar a imagem.");
        }
        return "/uploads/banners/" + nome;
    }

    private BannerFormato formato(String posicao) {
        try {
            return BannerFormato.valueOf(posicao == null ? "" : posicao.trim());
        } catch (IllegalArgumentException e) {
            throw erro("Posição inválida.");
        }
    }

    // Confere o conteúdo real do arquivo (não confia no nome nem no Content-Type).
    private String detectarExtensao(byte[] b) {
        boolean jpg = b.length >= 3
                && (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF;
        if (jpg) return "jpg";
        if (b.length >= 8 && Arrays.equals(Arrays.copyOf(b, 8), ASSINATURA_PNG)) return "png";
        throw erro("Formato inválido. Envie uma imagem JPG ou PNG.");
    }

    // Garante a proporção da posição e uma resolução mínima.
    private void validarDimensoes(byte[] bytes, int larguraEsperada, int alturaEsperada) {
        BufferedImage imagem;
        try {
            imagem = ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            throw erro("Não foi possível ler a imagem.");
        }
        if (imagem == null) {
            throw erro("Arquivo de imagem inválido.");
        }

        double esperada = (double) larguraEsperada / alturaEsperada;
        double real = (double) imagem.getWidth() / imagem.getHeight();
        if (Math.abs(real - esperada) / esperada > TOLERANCIA_PROPORCAO) {
            throw erro("Proporção incorreta. O esperado é " + larguraEsperada + "x" + alturaEsperada + ".");
        }
        if (imagem.getWidth() < larguraEsperada * LARGURA_MINIMA) {
            throw erro("Imagem muito pequena. O ideal é " + larguraEsperada + "x" + alturaEsperada + ".");
        }
    }

    private ResponseStatusException erro(String mensagem) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
    }
}