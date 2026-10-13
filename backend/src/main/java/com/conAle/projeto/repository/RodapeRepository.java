package com.conAle.projeto.repository;

import com.conAle.projeto.Model.Rodape;
import org.springframework.data.jpa.repository.JpaRepository;

// O Spring Data implementa métodos como save, findAll, findById e delete.
// Rodape é a entidade e Integer é o tipo de sua chave primária.
public interface RodapeRepository
        extends JpaRepository<Rodape, Integer> {
}