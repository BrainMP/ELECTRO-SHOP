    package com.electroshop.service;

    import com.electroshop.model.CartItem;
    import org.springframework.stereotype.Service;
    import org.springframework.web.context.annotation.SessionScope;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

    @Service
    @SessionScope
    public class CartService {

        private List<CartItem> items = new ArrayList<>();

        // ----------------------------------------------------
        // MÉTODO CLAVE: AÑADIR/CONSOLIDAR ITEM
        // ----------------------------------------------------
        public void addItem(CartItem newItem) {
            Long newProductId = newItem.getProducto().getId();

            // 1. Busca si el producto ya existe (CONSOLIDACIÓN)
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

        // ----------------------------------------------------
        // MÉTODO NUEVO 1: AUMENTAR LA CANTIDAD (+)
        // ----------------------------------------------------
        public void increaseQuantity(Long productoId) {
            Optional<CartItem> existingItem = items.stream()
                    .filter(item -> item.getProducto().getId().equals(productoId))
                    .findFirst();

            if (existingItem.isPresent()) {
                CartItem item = existingItem.get();
                item.setCantidad(item.getCantidad() + 1); // Incrementa en 1
            }
        }


        // ----------------------------------------------------
        // MÉTODO NUEVO 2: DISMINUIR LA CANTIDAD (-)
        // ----------------------------------------------------
        public void decreaseQuantity(Long productoId) {
            Optional<CartItem> existingItem = items.stream()
                    .filter(item -> item.getProducto().getId().equals(productoId))
                    .findFirst();

            if (existingItem.isPresent()) {
                CartItem item = existingItem.get();
                // Si la cantidad es mayor a 1, la disminuimos
                if (item.getCantidad() > 1) {
                    item.setCantidad(item.getCantidad() - 1); // Disminuye en 1
                } else {
                    // Si la cantidad es 1, al disminuirla la eliminamos por completo
                    removeItem(productoId); // Llama al método de eliminación total
                }
            }
        }

        // ----------------------------------------------------
        // MÉTODO DE ELIMINACIÓN TOTAL DE LÍNEA
        // ----------------------------------------------------
        public void removeItem(Long productoId) {
            // Elimina la única línea que contiene ese productoId
            items.removeIf(item -> item.getProducto().getId().equals(productoId));
        }

        // ----------------------------------------------------
        // MÉTODOS DE LECTURA
        // ----------------------------------------------------
        public List<CartItem> getItems() {
            return items;
        }

        public int getTotalQuantity() {
            return items.stream().mapToInt(CartItem::getCantidad).sum();
        }
    }