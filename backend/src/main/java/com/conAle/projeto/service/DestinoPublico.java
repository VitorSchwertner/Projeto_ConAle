package com.conAle.projeto.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

final class DestinoPublico {
    private static final Map<String, Set<String>> SECOES = Map.of(
            "/", Set.of("home", "sobre", "area-cliente", "servicos"),
            "/servicos", Set.of("lista-servicos"),
            "/sobre", Set.of("sobre", "area-cliente"),
            "/duvidas", Set.of("faq", "servicos")
    );

    private DestinoPublico() {}

    static String validar(String valor) {
        if (valor == null || valor.isBlank()) return null;
        String url = valor.strip();
        if (url.length() > 500) throw invalido("Cada link aceita até 500 caracteres.");

        URI destino;
        try {
            destino = new URI(url);
        } catch (URISyntaxException e) {
            throw invalido("Informe um link válido.");
        }

        if (("https".equalsIgnoreCase(destino.getScheme()) || "http".equalsIgnoreCase(destino.getScheme()))
                && destino.getHost() != null) {
            return url;
        }

        String secao = destino.getRawFragment();
        if (url.startsWith("#")) {
            if (secao != null && SECOES.values().stream().anyMatch(secoes -> secoes.contains(secao))) return url;
            throw invalido("Esta seção não existe no site. Use uma âncora como #servicos ou #faq.");
        }

        if (url.startsWith("/") && !url.startsWith("//")) {
            String caminho = destino.getRawPath().toLowerCase(Locale.ROOT).replaceFirst("/$", "");
            Set<String> secoes = SECOES.get(caminho.isEmpty() ? "/" : caminho);
            if (secoes == null) throw invalido("A página informada não existe no site.");
            if (secao != null && !secoes.contains(secao)) {
                throw invalido("A seção informada não existe nessa página.");
            }
            return url;
        }

        throw invalido("Use um endereço completo com https:// ou http://, um caminho como /servicos ou uma âncora como #faq.");
    }

    private static ResponseStatusException invalido(String mensagem) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
    }
}
