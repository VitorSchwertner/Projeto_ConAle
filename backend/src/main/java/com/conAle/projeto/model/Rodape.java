package com.conAle.projeto.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Representa a tabela rodape. As anotações indicam como o JPA acessa suas colunas.
@Entity
@Table(name = "rodape")
public class Rodape {

    // O MySQL gera o ID com AUTO_INCREMENT; antes de salvar, ele pode ser null.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Os limites acompanham a tabela; a validação da entrada fica no DTO.
    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    // Opcional: o rodapé pode ficar sem Instagram.
    @Column(name = "instagram", length = 100)
    private String instagram;

    @Column(name = "email", nullable = false, length = 254)
    private String email;

    @Column(name = "dias_semana", nullable = false, length = 100)
    private String diasSemana;

    @Column(name = "horas_trabalhadas", nullable = false, length = 100)
    private String horasTrabalhadas;

    @Column(name = "endereco", nullable = false, length = 255)
    private String endereco;

    public Integer getId() { return id; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getInstagram() { return instagram; }
    public void setInstagram(String instagram) { this.instagram = instagram; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiasSemana() { return diasSemana; }
    public void setDiasSemana(String diasSemana) { this.diasSemana = diasSemana; }

    public String getHorasTrabalhadas() { return horasTrabalhadas; }
    public void setHorasTrabalhadas(String horasTrabalhadas) { this.horasTrabalhadas = horasTrabalhadas; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
}