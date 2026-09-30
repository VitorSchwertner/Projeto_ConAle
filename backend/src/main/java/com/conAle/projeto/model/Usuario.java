package com.conAle.projeto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Representa a tabela usuario. As anotações indicam como o JPA acessa suas colunas.
@Entity
@Table(name = "usuario")
public class Usuario {

    // Contas antigas não recebem acesso administrativo automaticamente.
    @Column(nullable = false)
    private boolean administrador;

    public boolean isAdministrador() { return administrador; }
    public void setAdministrador(boolean administrador) { this.administrador = administrador; }

    // O MySQL gera o ID com AUTO_INCREMENT; antes de salvar, ele pode ser null.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Os limites acompanham a tabela; a validação da entrada fica no DTO.
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    // A coluna email é UNIQUE no banco; o service confere antes de gravar.
    @Column(name = "email", nullable = false, unique = true, length = 254)
    private String email;

    // Guarda apenas o hash BCrypt da senha, nunca o texto original.
    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }
}