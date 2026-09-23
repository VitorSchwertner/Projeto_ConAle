package com.conAle.projeto.model;

public class Banner
{
    private int id;
    private String titulo;
    private String imagem;
    private String link;
    private Boolean ativo;

    public String getTitulo()
    {
        return titulo;
    }

    public void setTitulo( String titulo )
    {
        this.titulo = titulo;
    }

    public String getImagem()
    {
        return imagem;
    }

    public void setImagem( String imagem )
    {
        this.imagem = imagem;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink( String link )
    {
        this.link = link;
    }

    public Boolean getAtivo()
    {
        return ativo;
    }

    public void setAtivo( Boolean ativo )
    {
        this.ativo = ativo;
    }
}
