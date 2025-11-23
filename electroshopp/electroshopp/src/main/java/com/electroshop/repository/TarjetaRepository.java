package com.electroshop.repository;

import com.electroshop.model.Tarjeta;
import com.electroshop.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {

    /**
     * Busca todas las tarjetas asociadas a un usuario específico.
     * Consulta: "SELECT * FROM tarjetas WHERE usuario_id = ?"
     */
    List<Tarjeta> findByUsuario(Usuario usuario);
}