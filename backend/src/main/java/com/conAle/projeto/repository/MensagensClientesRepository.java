package com.conAle.projeto.repository;

import com.conAle.projeto.Model.MensagensClientes;
import org.springframework.data.jpa.repository.JpaRepository;

// O Spring Data implementa métodos como save, findAll, findById e delete.
// MensagensClientes é a entidade e Integer é o tipo de sua chave primária.
public interface MensagensClientesRepository
        extends JpaRepository<MensagensClientes, Integer> {
}