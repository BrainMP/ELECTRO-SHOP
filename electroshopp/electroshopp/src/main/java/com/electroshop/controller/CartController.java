package com.electroshop.controller;

import com.electroshop.model.CarritoItem; // Ahora usamos el CarritoItem de la BD
import com.electroshop.model.Producto;
import com.electroshop.model.Tarjeta;
import com.electroshop.model.Usuario;
import com.electroshop.repository.ProductoRepository;
import com.electroshop.repository.TarjetaRepository;
import com.electroshop.repository.UsuarioRepository;
import com.electroshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.security.Principal; // (1) IMPORTAR PRINCIPAL
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CartService cartService; // Este es el nuevo CartService v2

    @Autowired
    private TarjetaRepository tarjetaRepository; // (1) INYECTAR REPOSITORIO DE TARJETAS

    @Autowired
    private UsuarioRepository usuarioRepository; // (2) INYECTAR REPOSITORIO DE USUARIO
    // ----------------------------------------------------
    // AÑADIR PRODUCTO (MODIFICADO)
    // ----------------------------------------------------
    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam("productoId") Long productoId,
            @RequestParam(value = "cantidad", defaultValue = "1") int cantidad,
            @RequestParam(value = "redirectUrl", defaultValue = "/") String redirectUrl,
            RedirectAttributes redirectAttributes,
            Principal principal // (2) OBTENER USUARIO LOGUEADO
    ) {
        // (Spring Security ya bloquea esto si 'principal' es null,
        // pero es una buena práctica verificar)
        if (principal == null) {
            return "redirect:/login";
        }
        String email = principal.getName(); // Obtener email

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado. ID: " + productoId));

        // (3) Llamar al nuevo método del servicio
        cartService.addItem(producto, cantidad, email);

        redirectAttributes.addFlashAttribute("mensaje", "¡Producto añadido al carrito con éxito!");
        if (redirectUrl.startsWith("/producto/")) {
            return "redirect:" + redirectUrl;
        } else {
            // Para todas las demás vistas (Catálogo, Home, etc.), regresa a la URL base.
            return "redirect:" + redirectUrl;
        }
    }

    // ----------------------------------------------------
    // VER CONTENIDO DEL CARRITO (MODIFICADO)
    // ----------------------------------------------------
    @GetMapping("/cart")
    public String viewCart(Model model, Principal principal) { // (2) OBTENER USUARIO

        if (principal == null) {
            // Si el usuario llega aquí sin estar logueado, redirigir
            return "redirect:/login";
        }
        String email = principal.getName();

        // (3) Llamar a los nuevos métodos del servicio
        List<CarritoItem> items = cartService.getItems(email);
        BigDecimal total = cartService.getTotalPrice(email);

        // (B) Obtener la lista de tarjetas del usuario logueado
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario != null) {
            List<Tarjeta> tarjetas = tarjetaRepository.findByUsuario(usuario);
            model.addAttribute("tarjetas", tarjetas); // Enviar las tarjetas a la vista
        }
        model.addAttribute("items", items);
        model.addAttribute("total", total);

        return "cart-view"; // (Tu plantilla cart-view.html)
    }

    // ----------------------------------------------------
    // INCREMENTAR CANTIDAD (MODIFICADO)
    // ----------------------------------------------------
    @GetMapping("/cart/increase/{productoId}")
    public String increaseQuantity(
            @PathVariable("productoId") Long productoId,
            Principal principal // (2) OBTENER USUARIO
    ) {
        if (principal == null) { return "redirect:/login"; }

        // (3) Llamar al nuevo método del servicio
        cartService.increaseQuantity(productoId, principal.getName());
        return "redirect:/cart";
    }

    // ----------------------------------------------------
    // DISMINUIR CANTIDAD (MODIFICADO)
    // ----------------------------------------------------
    @GetMapping("/cart/decrease/{productoId}")
    public String decreaseQuantity(
            @PathVariable("productoId") Long productoId,
            Principal principal // (2) OBTENER USUARIO
    ) {
        if (principal == null) { return "redirect:/login"; }

        // (3) Llamar al nuevo método del servicio
        cartService.decreaseQuantity(productoId, principal.getName());
        return "redirect:/cart";
    }

    // ----------------------------------------------------
    // ELIMINAR LÍNEA (MODIFICADO)
    // ----------------------------------------------------
    @GetMapping("/cart/remove/{productoId}")
    public String removeItem(
            @PathVariable("productoId") Long productoId,
            RedirectAttributes redirectAttributes,
            Principal principal // (2) OBTENER USUARIO
    ) {
        if (principal == null) { return "redirect:/login"; }

        // (3) Llamar al nuevo método del servicio
        cartService.removeItem(productoId, principal.getName());

        redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado del carrito.");
        return "redirect:/cart";
    }
}