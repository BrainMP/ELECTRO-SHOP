package com.electroshop.repository;

import com.electroshop.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

// ... imports
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Puedes añadir este método para buscar si una categoría ya existe
    Categoria findByNombre(String nombre);
}
