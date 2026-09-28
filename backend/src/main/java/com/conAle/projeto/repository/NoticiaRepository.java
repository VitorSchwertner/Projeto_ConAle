package com.conAle.projeto.repository;

import com.conAle.projeto.model.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;

// O Spring Data implementa métodos como save, findAll, findById e delete.
// Noticia é a entidade e Integer é o tipo de sua chave primária.
public interface NoticiaRepository
        extends JpaRepository<Noticia, Integer> {
}
