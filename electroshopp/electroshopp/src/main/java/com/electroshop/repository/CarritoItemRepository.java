package com.electroshop.repository;

import com.electroshop.model.Carrito;
import com.electroshop.model.CarritoItem;
import com.electroshop.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {

    /**
     * Busca un item específico dentro de un carrito, basado en el producto.
     * Lo usaremos para la lógica de "consolidación" (sumar cantidad si el producto ya existe).
     * Consulta: "SELECT * FROM carrito_items WHERE carrito_id = ? AND producto_id = ?"
     */
    Optional<CarritoItem> findByCarritoAndProducto(Carrito carrito, Producto producto);
}
