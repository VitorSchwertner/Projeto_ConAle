package com.conAle.projeto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

// Torna a pasta de uploads acessível pelo navegador em /uploads/**.
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String pastaUpload;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String local = Path.of(pastaUpload).toAbsolutePath().normalize().toUri().toString();
        if (!local.endsWith("/")) {
            local += "/";
        }
        registry.addResourceHandler("/uploads/**").addResourceLocations(local);
    }
}