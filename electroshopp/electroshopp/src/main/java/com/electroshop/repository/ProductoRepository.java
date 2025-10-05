package com.electroshop.repository;

import com.electroshop.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Hereda las funciones básicas (findAll, save, findById)
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Spring Data JPA crea esta consulta automáticamente:
    // Busca en Nombre O en Descripción, ignorando mayúsculas/minúsculas.
    List<Producto> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String nombre, String descripcion);
    // 2. Funcionalidad de Filtrado por Categoría (¡NUEVO Y NECESARIO!)
    // Busca en el campo 'categoria' y luego en su propiedad 'nombre'.
    List<Producto> findByCategoria_Nombre(String nombreCategoria);
}
