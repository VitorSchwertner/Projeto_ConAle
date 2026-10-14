package com.conAle.projeto.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Representa a tabela indicador_institucional. As anotações indicam como o JPA acessa suas colunas.
@Entity
@Table(name = "indicador_institucional")
public class IndicadorInstitucional {

    // O MySQL gera o ID com AUTO_INCREMENT; antes de salvar, ele pode ser null.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "anos_atuacao", nullable = false)
    private int anosAtuacao;

    @Column(name = "empresas_abertas", nullable = false)
    private int empresasAbertas;

    public Integer getId() { return id; }

    public int getAnosAtuacao() { return anosAtuacao; }
    public void setAnosAtuacao(int anosAtuacao) { this.anosAtuacao = anosAtuacao; }

    public int getEmpresasAbertas() { return empresasAbertas; }
    public void setEmpresasAbertas(int empresasAbertas) { this.empresasAbertas = empresasAbertas; }
}