package com.conAle.projeto.repository;

import com.conAle.projeto.Model.Banner;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// O Spring Data implementa métodos como save, findAll, findById e delete.
// Banner é a entidade e Integer é o tipo de sua chave primária.
public interface BannerRepository
        extends JpaRepository<Banner, Integer> {

    // O Spring gera a consulta a partir do nome do método.
    // Usado para listar apenas os banners que devem aparecer no site.
    List<Banner> findByAtivoTrue( Sort sort);
}