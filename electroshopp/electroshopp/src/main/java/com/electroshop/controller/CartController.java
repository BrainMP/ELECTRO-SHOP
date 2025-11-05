package com.electroshop.controller;

import com.electroshop.model.CartItem;
import com.electroshop.model.Producto;
import com.electroshop.repository.ProductoRepository;
import com.electroshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable; // Necesario para {productoId}

import java.math.BigDecimal;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CartService cartService;

    // ----------------------------------------------------
    // AÑADIR PRODUCTO AL CARRITO (Acción POST)
    // ----------------------------------------------------
    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam("productoId") Long productoId,
            @RequestParam(value = "cantidad", defaultValue = "1") int cantidad,
            RedirectAttributes redirectAttributes
    ) {
        // 1. Busca el producto
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado. ID: " + productoId));

        // 2. Crea y añade el ítem (la lógica de consolidación está en CartService)
        CartItem newItem = new CartItem(producto, cantidad);
        cartService.addItem(newItem);

        // 3. Muestra mensaje de éxito y redirige
        redirectAttributes.addFlashAttribute("mensaje", "¡Producto añadido al carrito con éxito!");
        return "redirect:/";
    }

    // ----------------------------------------------------
    // VER CONTENIDO DEL CARRITO (Vista GET)
    // ----------------------------------------------------
    @GetMapping("/cart")
    public String viewCart(Model model) {

        List<CartItem> items = cartService.getItems();

        model.addAttribute("items", items);
        model.addAttribute("total", calcularTotal(items));

        return "cart-view";
    }

    // ----------------------------------------------------
    // LÓGICA: CALCULAR EL TOTAL DEL CARRITO (Método auxiliar)
    // ----------------------------------------------------
    private BigDecimal calcularTotal(List<CartItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            BigDecimal subtotal = item.getProducto().getPrecio()
                    .multiply(new BigDecimal(item.getCantidad()));
            total = total.add(subtotal);
        }
        return total;
    }

    // ====================================================
    // === NUEVOS ENDPOINTS PARA CONTROL DE CANTIDAD (+/-) ==
    // ====================================================

    // 1. ENDPOINT PARA AUMENTAR CANTIDAD (+)
    @GetMapping("/cart/increase/{productoId}")
    public String increaseQuantity(@PathVariable("productoId") Long productoId) {
        // Llama al servicio para incrementar la cantidad de la línea consolidada
        cartService.increaseQuantity(productoId);
        return "redirect:/cart";
    }

    // 2. ENDPOINT PARA DISMINUIR CANTIDAD (-)
    // Esta función también maneja la eliminación si la cantidad baja a cero.
    @GetMapping("/cart/decrease/{productoId}")
    public String decreaseQuantity(@PathVariable("productoId") Long productoId) {
        // Llama al servicio para decrementar la cantidad
        cartService.decreaseQuantity(productoId);
        return "redirect:/cart";
    }

    // 3. REMOVEDOR DE PRODUCTOS (Mantendremos esta ruta simple para eliminación total,
    // pero la haremos GET para que coincida con los demás enlaces de control)
    @GetMapping("/cart/remove/{productoId}")
    public String removeItem(@PathVariable("productoId") Long productoId, RedirectAttributes redirectAttributes) {
        cartService.removeItem(productoId);
        redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado del carrito.");
        return "redirect:/cart";
    }
    // SEGUIR COMPRANDO - Redirige al catálogo principal

    @GetMapping("/cart/continue-shopping")
    public String continueShopping() {
        return "redirect:/"; // Redirige al catálogo principal
    }

// PAGAR - Endpoint temporal

    @PostMapping("/pagar")
    public String procesarPago(RedirectAttributes redirectAttributes) {
        // Lógica temporal de pago
        redirectAttributes.addFlashAttribute("mensaje", "¡Pedido procesado con éxito!");
        cartService.getItems().clear(); // Limpiar carrito después del pago
        return "redirect:/";
    }
}