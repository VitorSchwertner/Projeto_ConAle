package com.conAle.projeto.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LinkSiteService {
    private final JdbcTemplate jdbc;

    public LinkSiteService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Map<String, String> listar() {
        return jdbc.query("SELECT codigo, url FROM link_site WHERE url IS NOT NULL", rows -> {
            Map<String, String> links = new HashMap<>();
            while (rows.next()) {
                links.put(rows.getString("codigo"), rows.getString("url"));
            }
            return links;
        });
    }

    @Transactional
    public void atualizar(Map<String, String> links) {
        var codigos = new HashSet<>(jdbc.queryForList("SELECT codigo FROM link_site", String.class));
        Map<String, String> destinos = new TreeMap<>();
        links.forEach((codigo, url) -> {
            if (!codigos.contains(codigo)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Botão ou serviço desconhecido.");
            }
            destinos.put(codigo, DestinoPublico.validar(url));
        });
        destinos.forEach((codigo, url) -> jdbc.update("UPDATE link_site SET url = ? WHERE codigo = ?", url, codigo));
    }
}
