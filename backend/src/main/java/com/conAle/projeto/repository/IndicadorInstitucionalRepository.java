package com.conAle.projeto.repository;

import com.conAle.projeto.Model.IndicadorInstitucional;
import org.springframework.data.jpa.repository.JpaRepository;

// O Spring Data implementa métodos como save, findAll, findById e delete.
// IndicadorInstitucional é a entidade e Integer é o tipo de sua chave primária.
public interface IndicadorInstitucionalRepository
        extends JpaRepository<IndicadorInstitucional, Integer> {
}