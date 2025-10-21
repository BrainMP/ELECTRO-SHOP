package com.electroshop.service;

import com.electroshop.model.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope; // Importante para la sesión
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@SessionScope // ¡CRUCIAL! Le dice a Spring que esta clase vive en la sesión del usuario
public class CartService {

    // Lista para almacenar todos los CartItem del usuario actual
    private List<CartItem> items = new ArrayList<>();

    // Este es el primer método clave: añadir un ítem.
    // Lo simplificaremos para evitar lógica compleja de actualización por ahora.
    public void addItem(CartItem newItem) {
        Long newProductId = newItem.getProducto().getId();

        // 1. Buscar si el producto ya existe en la lista de la sesión
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getProducto().getId().equals(newProductId))
                .findFirst();

        if (existingItem.isPresent()) {
            // 2. Si ya existe, SUMAR la cantidad
            CartItem item = existingItem.get();
            item.setCantidad(item.getCantidad() + newItem.getCantidad());
        } else {
            // 3. Si es nuevo, simplemente agregarlo
            items.add(newItem);
        }
    }

    // Método para obtener el contenido del carrito
    public List<CartItem> getItems() {
        return items;
    }
    // Agrega este método para que el GlobalControllerAdvice pueda contar
    public int getTotalQuantity() {
        // Si CartItem tiene la cantidad, sumamos la cantidad de todos los ítems
        return items.stream().mapToInt(item -> item.getCantidad()).sum();
    }

    //Metodo para remover productos agregados al carrito
    public void removeItem(Long productoId) {
        // Busca y elimina el CartItem de la lista por su ID de Producto.
        items.removeIf(item -> item.getProducto().getId().equals(productoId));
    }

}