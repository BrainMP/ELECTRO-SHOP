package com.electroshop.service;

import com.electroshop.model.Carrito;
import com.electroshop.model.CarritoItem;
import com.electroshop.model.Producto;
import com.electroshop.model.Usuario;
import com.electroshop.repository.CarritoItemRepository;
import com.electroshop.repository.CarritoRepository;
import com.electroshop.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Versión 2.0 del CartService.
 * Ya NO usa @SessionScope.
 * Ahora interactúa directamente con la base de datos (persiste el carrito).
 */
@Service
public class CartService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // --- Métodos de Ayuda Internos ---

    /**
     * Busca al usuario logueado en la BD.
     */
    private Usuario getUsuario(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    }

    /**
     * Busca el carrito del usuario. Si no existe, crea uno nuevo.
     */
    private Carrito getOrCreateCarrito(Usuario usuario) {
        // Busca el carrito por usuario. Si no lo encuentra (.orElseGet), crea uno nuevo.
        return carritoRepository.findByUsuario(usuario).orElseGet(() -> {
            Carrito nuevoCarrito = new Carrito();
            nuevoCarrito.setUsuario(usuario);
            return carritoRepository.save(nuevoCarrito);
        });
    }

    // --- Métodos Públicos (Llamados por el Controlador) ---

    /**
     * Obtiene todos los items del carrito del usuario logueado.
     * @param userEmail El email (Principal.getName()) del usuario logueado.
     */
    public List<CarritoItem> getItems(String userEmail) {
        Usuario usuario = getUsuario(userEmail);
        Carrito carrito = getOrCreateCarrito(usuario);
        return carrito.getItems();
    }

    /**
     * Añade un producto al carrito persistente.
     * Si el producto ya existe, incrementa la cantidad.
     * @param producto El Producto a añadir.
     * @param cantidad La cantidad a añadir.
     * @param userEmail El email del usuario logueado.
     */
    @Transactional
    public void addItem(Producto producto, int cantidad, String userEmail) {
        Usuario usuario = getUsuario(userEmail);
        Carrito carrito = getOrCreateCarrito(usuario);

        // 1. Verificar si el item (producto) ya existe en este carrito
        Optional<CarritoItem> itemExistente = carritoItemRepository.findByCarritoAndProducto(carrito, producto);

        if (itemExistente.isPresent()) {
            // 2. Si existe, actualizar la cantidad
            CarritoItem item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            carritoItemRepository.save(item); // Guarda el item actualizado
        } else {
            // 3. Si no existe, crear un nuevo CarritoItem
            CarritoItem nuevoItem = new CarritoItem();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(cantidad);
            carritoItemRepository.save(nuevoItem); // Guarda el nuevo item
        }
    }

    /**
     * Incrementa la cantidad de un producto en el carrito.
     */
    @Transactional
    public void increaseQuantity(Long productoId, String userEmail) {
        Usuario usuario = getUsuario(userEmail);
        Carrito carrito = getOrCreateCarrito(usuario);

        Optional<CarritoItem> itemOpt = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst();

        if (itemOpt.isPresent()) {
            CarritoItem item = itemOpt.get();
            item.setCantidad(item.getCantidad() + 1);
            carritoItemRepository.save(item);
        }
    }

    /**
     * Disminuye la cantidad. Si llega a 0, elimina el item.
     */
    @Transactional
    public void decreaseQuantity(Long productoId, String userEmail) {
        Usuario usuario = getUsuario(userEmail);
        Carrito carrito = getOrCreateCarrito(usuario);

        Optional<CarritoItem> itemOpt = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst();

        if (itemOpt.isPresent()) {
            CarritoItem item = itemOpt.get();
            if (item.getCantidad() > 1) {
                item.setCantidad(item.getCantidad() - 1);
                carritoItemRepository.save(item);
            } else {
                // Si la cantidad es 1, eliminamos el item
                carrito.getItems().remove(item); // Quita de la lista en memoria
                carritoItemRepository.delete(item); // Borra de la BD
            }
        }
    }

    /**
     * Elimina una línea de producto completa del carrito, sin importar la cantidad.
     */
    @Transactional
    public void removeItem(Long productoId, String userEmail) {
        Usuario usuario = getUsuario(userEmail);
        Carrito carrito = getOrCreateCarrito(usuario);

        Optional<CarritoItem> itemOpt = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst();

        if (itemOpt.isPresent()) {
            CarritoItem item = itemOpt.get();
            carrito.getItems().remove(item);
            carritoItemRepository.delete(item);
        }
    }

    /**
     * Calcula el total de items (para el contador del header).
     */
    public int getTotalQuantity(String userEmail) {
        if (userEmail == null) {
            return 0; // Si no hay usuario logueado, el carrito es 0
        }
        try {
            Usuario usuario = getUsuario(userEmail);
            Carrito carrito = getOrCreateCarrito(usuario);
            // Suma la cantidad de cada línea de item
            return carrito.getItems().stream().mapToInt(CarritoItem::getCantidad).sum();
        } catch (UsernameNotFoundException e) {
            return 0; // Usuario no encontrado (raro, pero seguro)
        }
    }

    /**
     * Calcula el precio total del carrito.
     */
    public BigDecimal getTotalPrice(String userEmail) {
        if (userEmail == null) {
            return BigDecimal.ZERO;
        }
        try {
            Usuario usuario = getUsuario(userEmail);
            Carrito carrito = getOrCreateCarrito(usuario);

            BigDecimal total = BigDecimal.ZERO;
            for (CarritoItem item : carrito.getItems()) {
                BigDecimal subtotal = item.getProducto().getPrecio()
                        .multiply(new BigDecimal(item.getCantidad()));
                total = total.add(subtotal);
            }
            return total;
        } catch (UsernameNotFoundException e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * Limpia el carrito (usado después del checkout).
     */
    @Transactional
    public void clearCart(String userEmail) {
        Usuario usuario = getUsuario(userEmail);
        Optional<Carrito> carritoOpt = carritoRepository.findByUsuario(usuario);

        if (carritoOpt.isPresent()) {
            Carrito carrito = carritoOpt.get();
            // Borra todos los items asociados a este carrito
            carritoItemRepository.deleteAll(carrito.getItems());
            carrito.getItems().clear();
            carritoRepository.save(carrito);
        }
    }
}