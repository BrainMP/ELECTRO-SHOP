package com.electroshop.repository;

import com.electroshop.model.Orden;
import com.electroshop.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {

    /**
     * Define un método de búsqueda personalizado.
     * Spring Data JPA creará automáticamente la consulta SQL:
     * "SELECT * FROM ordenes WHERE usuario_id = ? ORDER BY fechaCreacion DESC"
     *
     * @param usuario El objeto Usuario por el cual filtrar los pedidos.
     * @return Una lista de pedidos pertenecientes a ese usuario, ordenados por fecha.
     */
    List<Orden> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);
}