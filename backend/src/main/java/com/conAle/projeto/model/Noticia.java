package com.conAle.projeto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Representa a tabela noticia. As anotações indicam como o JPA acessa suas colunas.
@Entity
@Table(name = "noticia")
public class Noticia {

    // O MySQL gera o ID com AUTO_INCREMENT; antes de salvar, ele pode ser null.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Os limites acompanham a tabela; a validação da entrada fica no DTO.
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    // Liga o nome usado no Java à coluna sub_titulo, que aceita null.
    @Column(name = "sub_titulo", length = 500)
    private String subTitulo;

    public Integer getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubTitulo() {
        return subTitulo;
    }

    public void setSubTitulo(String subTitulo) {
        this.subTitulo = subTitulo;
    }
}
