package com.electroshop.repository;

import com.electroshop.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Define un método de búsqueda personalizado.
     * Spring Data JPA creará automáticamente la consulta SQL:
     * "SELECT * FROM usuarios WHERE email = ?"
     * * @param email El email (username) que Spring Security usará para buscar al usuario.
     * @return Un Optional que contendrá al Usuario si se encuentra.
     */
    Optional<Usuario> findByEmail(String email);
}
