package com.electroshop.service;

import com.electroshop.model.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope; // Importante para la sesión
import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope // ¡CRUCIAL! Le dice a Spring que esta clase vive en la sesión del usuario
public class CartService {

    // Lista para almacenar todos los CartItem del usuario actual
    private List<CartItem> items = new ArrayList<>();

    // Este es el primer método clave: añadir un ítem.
    // Lo simplificaremos para evitar lógica compleja de actualización por ahora.
    public void addItem(CartItem item) {
        items.add(item);
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

    // Método para calcular el total
    // NOTA: Para calcular el total necesitamos la lógica del Producto (precio * cantidad)

}