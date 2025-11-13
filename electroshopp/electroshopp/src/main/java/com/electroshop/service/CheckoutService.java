package com.electroshop.service;

import com.electroshop.model.*; // Importa todos los modelos (CarritoItem, Orden, etc.)
import com.electroshop.repository.OrdenRepository;
import com.electroshop.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Service
public class CheckoutService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CartService cartService; // Este es el CartService v2

    /**
     * Procesa la compra.
     * 1. Obtiene el usuario.
     * 2. Transforma el carrito de BD en un pedido de BD.
     * 3. Guarda el pedido.
     * 4. Limpia el carrito de BD.
     */
    @Transactional
    public Orden procesarPedido(Principal principal) {

        // 1. Obtener el Usuario
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));

        // 2. Obtener el Carrito
        // ¡¡CORRECCIÓN 1!! - Pasamos el email al método getItems
        List<CarritoItem> itemsDelCarrito = cartService.getItems(email);

        if (itemsDelCarrito.isEmpty()) {
            throw new RuntimeException("El carrito está vacío.");
        }

        // 3. Crear una Orden
        Orden nuevaOrden = new Orden();
        nuevaOrden.setUsuario(usuario);

        BigDecimal montoTotal = BigDecimal.ZERO;

        // 4. Copiar los Productos (CarritoItem -> OrdenDetalle)
        for (CarritoItem item : itemsDelCarrito) {
            OrdenDetalle detalle = new OrdenDetalle();

            detalle.setNombreProducto(item.getProducto().getNombre());
            detalle.setModeloProducto(item.getProducto().getModelo());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getProducto().getPrecio());
            detalle.setImagenUrl(item.getProducto().getImagenUrl()); // Copiamos la imagen

            BigDecimal subtotal = item.getProducto().getPrecio().multiply(new BigDecimal(item.getCantidad()));
            detalle.setSubtotal(subtotal);

            montoTotal = montoTotal.add(subtotal);

            // 5. Asociar el detalle con la orden principal
            nuevaOrden.addDetalle(detalle);
        }

        nuevaOrden.setMontoTotal(montoTotal);

        // 6. Guardar en la Base de Datos
        Orden ordenGuardada = ordenRepository.save(nuevaOrden);

        // 7. Limpiar el Carrito (SOLO después de guardar)
        // ¡¡CORRECCIÓN 2!! - Llamamos al nuevo método clearCart con el email
        cartService.clearCart(email);

        return ordenGuardada;
    }
}