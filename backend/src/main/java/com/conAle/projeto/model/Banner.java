package com.conAle.projeto.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Representa a tabela banner. As anotações indicam como o JPA acessa suas colunas.
@Entity
@Table(name = "banner")
public class Banner {

    // O MySQL gera o ID com AUTO_INCREMENT; antes de salvar, ele pode ser null.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Os limites acompanham a tabela; a validação da entrada fica no DTO.
    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    // Guarda o caminho ou a URL da imagem, não o arquivo em si.
    @Column(name = "imagem", nullable = false, length = 500)
    private String imagem;

    // Versão para celular. Opcional: sem ela, o site usa a imagem principal.
    @Column(name = "imagem_mobile", length = 500)
    private String imagemMobile;

    // Opcional: o banner pode ser apenas visual, sem link.
    @Column(name = "link", length = 500)
    private String link;

    // Identifica em qual seção do site o banner deve aparecer.
    // Registros antigos podem permanecer sem posição até serem editados.
    @Column(name = "posicao", length = 30)
    private String posicao;

    // Indica se o banner deve ser exibido no site.
    @Column(name = "ativo", nullable = false)
    private boolean ativo;

    public Integer getId() { return id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }

    public String getImagemMobile() { return imagemMobile; }
    public void setImagemMobile(String imagemMobile) { this.imagemMobile = imagemMobile; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getPosicao() { return posicao; }
    public void setPosicao(String posicao) { this.posicao = posicao; }
    public boolean aceitaLink() { return !"SOBRE_MIM_FOTO".equals(posicao); }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
