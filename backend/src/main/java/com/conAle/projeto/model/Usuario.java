package com.conAle.projeto.model;

public class Usuario
{
    private int id;
    private String nome;
    private String email;
    private String senhaHash;

    public String getNome()
    {
        return nome;
    }

    public void setNome( String nome )
    {
        this.nome = nome;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getSenhaHash()
    {
        return senhaHash;
    }

    public void setSenhaHash( String senhaHash )
    {
        this.senhaHash = senhaHash;
    }
}
