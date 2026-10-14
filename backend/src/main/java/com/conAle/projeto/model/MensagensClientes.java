package com.conAle.projeto.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Representa a tabela mensagens_clientes. As anotações indicam como o JPA acessa suas colunas.
@Entity
@Table(name = "mensagens_clientes")
public class MensagensClientes {

    // O MySQL gera o ID com AUTO_INCREMENT; antes de salvar, ele pode ser null.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Os limites acompanham a tabela; a validação da entrada fica no DTO.
    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "area_atuacao", nullable = false, length = 150)
    private String areaAtuacao;

    @Column(name = "nota", nullable = false)
    private Double nota;

    @Column(name = "mensagem", nullable = false, length = 1000)
    private String mensagem;

    public Integer getId() { return id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAreaAtuacao() { return areaAtuacao; }
    public void setAreaAtuacao(String areaAtuacao) { this.areaAtuacao = areaAtuacao; }

    public Double getNota() { return nota; }
    public void setNota(Double nota) { this.nota = nota; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}