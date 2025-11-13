package com.electroshop.repository;

import com.electroshop.model.Carrito;
import com.electroshop.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    /**
     * Busca un carrito basado en el objeto Usuario.
     * Spring Data JPA crea la consulta: "SELECT * FROM carritos WHERE usuario_id = ?"
     */
    Optional<Carrito> findByUsuario(Usuario usuario);
}
