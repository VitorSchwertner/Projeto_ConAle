package com.conAle.projeto.repository;

import com.conAle.projeto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

// O Spring Data implementa métodos como save, findAll, findById e delete.
// Usuario é a entidade e Integer é o tipo de sua chave primária.
public interface UsuarioRepository
        extends JpaRepository<Usuario, Integer> {

    java.util.Optional<Usuario> findByEmail(String email);
    boolean existsByAdministradorTrue();
    // Serializa exclusões administrativas para proteger também contra pedidos simultâneos.
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select u from Usuario u where u.administrador = true order by u.id")
    java.util.List<Usuario> administradoresParaExclusao();

    // O Spring gera a consulta a partir do nome do método.
    boolean existsByEmail(String email);

    // Usado na edição: ignora o próprio usuário ao procurar e-mail repetido.
    boolean existsByEmailAndIdNot(String email, Integer id);
}
