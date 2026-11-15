package com.conAle.projeto.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// A migration cria a configuração única do rodapé exibido no site.
@Entity
@Table(name = "rodape_ativo")
public class RodapeAtivo {
    @Id
    private Integer id;

    @Column(name = "rodape_id", nullable = false)
    private Integer rodapeId;

    public Integer getRodapeId() { return rodapeId; }
    public void setRodapeId(Integer rodapeId) { this.rodapeId = rodapeId; }
}
