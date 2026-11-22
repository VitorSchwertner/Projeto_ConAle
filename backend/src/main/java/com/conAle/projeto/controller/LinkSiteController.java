package com.conAle.projeto.Controller;

import com.conAle.projeto.service.LinkSiteService;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/links-site")
public class LinkSiteController {
    private final LinkSiteService service;

    public LinkSiteController(LinkSiteService service) {
        this.service = service;
    }

    @GetMapping
    public Map<String, String> listar() {
        return service.listar();
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@RequestBody Map<String, String> links) {
        service.atualizar(links);
    }
}
