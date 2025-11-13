package com.electroshop.controller;

import com.electroshop.model.Orden;
import com.electroshop.model.Usuario;
import com.electroshop.repository.OrdenRepository;
import com.electroshop.repository.UsuarioRepository;
import com.electroshop.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Procesa el clic en "PAGAR AHORA".
     * Llama al CheckoutService para convertir el carrito de sesión en un pedido de BD.
     */
    @PostMapping("/checkout") // Esta ruta debe coincidir con el <form> del botón "PAGAR AHORA"
    public String procesarPago(Principal principal, RedirectAttributes redirectAttributes) {

        if (principal == null) {
            // Seguridad extra: si no hay usuario, redirigir al login
            return "redirect:/login";
        }

        try {
            // 1. Llama al servicio que hace todo el trabajo
            Orden ordenGuardada = checkoutService.procesarPedido(principal);

            // 2. Envía un mensaje de éxito a la siguiente página
            redirectAttributes.addFlashAttribute("pedidoExitoso", "¡Gracias por tu compra! Tu pedido #" + ordenGuardada.getId() + " ha sido procesado.");

            // 3. Redirige al catálogo (o a una página de "éxito")
            return "redirect:/";

        } catch (RuntimeException e) {
            // Maneja errores (ej. si el carrito estaba vacío)
            redirectAttributes.addFlashAttribute("pedidoError", e.getMessage());
            return "redirect:/cart"; // Devuelve al carrito si algo salió mal
        }
    }

    /**
     * Muestra la página "Mis Pedidos" del usuario.
     * Este es el método que hace funcionar el enlace del menú de perfil.
     */
    @GetMapping("/pedidos")
    public String mostrarPedidos(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        // 1. Obtener el email y buscar al usuario
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));

        // 2. Buscar en la BD todos los pedidos de ESE usuario (usando el repo que creamos)
        List<Orden> pedidos = ordenRepository.findByUsuarioOrderByFechaCreacionDesc(usuario);

        // 3. Enviar la lista de pedidos a la vista
        model.addAttribute("pedidos", pedidos);

        return "pedidos"; // 4. Busca el archivo templates/pedidos.html
    }
}
