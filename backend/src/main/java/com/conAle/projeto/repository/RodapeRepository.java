package com.conAle.projeto.repository;

import com.conAle.projeto.Model.Rodape;
import com.conAle.projeto.Model.RodapeAtivo;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// O Spring Data implementa métodos como save, findAll, findById e delete.
// Rodape é a entidade e Integer é o tipo de sua chave primária.
public interface RodapeRepository
        extends JpaRepository<Rodape, Integer> {
    @Query("select r from Rodape r, RodapeAtivo a where a.id = 1 and r.id = a.rodapeId")
    Optional<Rodape> buscarAtivo();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from RodapeAtivo a where a.id = 1")
    Optional<RodapeAtivo> bloquearSelecao();
}
