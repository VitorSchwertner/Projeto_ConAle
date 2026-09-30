package com.conAle.projeto.repository;

import com.conAle.projeto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

// O Spring Data implementa métodos como save, findAll, findById e delete.
// Usuario é a entidade e Integer é o tipo de sua chave primária.
public interface UsuarioRepository
        extends JpaRepository<Usuario, Integer> {

    // O Spring gera a consulta a partir do nome do método.
    boolean existsByEmail(String email);

    // Usado na edição: ignora o próprio usuário ao procurar e-mail repetido.
    boolean existsByEmailAndIdNot(String email, Integer id);
}