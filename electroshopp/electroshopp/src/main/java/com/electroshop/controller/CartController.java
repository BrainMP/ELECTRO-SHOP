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

        // 2. Crea y añade el ítem
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

        // ¡ACTUALIZACIÓN! Usamos el método de cálculo e incluimos el resultado en el modelo
        model.addAttribute("total", calcularTotal(items));

        return "cart-view"; // Busca la plantilla cart-view.html
    }

    // ----------------------------------------------------
    // LÓGICA: CALCULAR EL TOTAL DEL CARRITO (Método auxiliar)
    // ----------------------------------------------------
    private BigDecimal calcularTotal(List<CartItem> items) {

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : items) {
            // Utilizamos el método getProducto() y getCantidad() del CartItem
            BigDecimal subtotal = item.getProducto().getPrecio()
                    .multiply(new BigDecimal(item.getCantidad()));

            total = total.add(subtotal);
        }

        return total;
    }

    //CartController removerdor de productos en el carrito
    @PostMapping("/cart/remove")
    public String removeFromCart(
            @RequestParam("productoId") Long productoId,
            RedirectAttributes redirectAttributes
    ) {
        cartService.removeItem(productoId);

        // Redirige de vuelta a la vista del carrito
        redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado del carrito.");
        return "redirect:/cart";
    }
// ...
}