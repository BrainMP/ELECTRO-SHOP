package com.electroshop.controller;

import com.electroshop.model.Orden;
import com.electroshop.model.Usuario;
import com.electroshop.repository.OrdenRepository;
import com.electroshop.repository.UsuarioRepository;
import com.electroshop.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/pedidos")
    public String mostrarPedidos(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        try {
            String email = principal.getName();
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));

            List<Orden> pedidos = ordenRepository.findByUsuarioOrderByFechaCreacionDesc(usuario);
            List<Orden> pedidosActivos = pedidos.stream()
                    .filter(pedido -> !pedido.isArchivado())
                    .toList();

            model.addAttribute("pedidos", pedidosActivos);
            return "pedidos";

        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los pedidos: " + e.getMessage());
            return "pedidos";
        }
    }

    @GetMapping("/pedidos/{id}")
    public String verDetallePedido(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        try {
            String email = principal.getName();
            Orden orden = ordenRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            if (!orden.getUsuario().getEmail().equals(email)) {
                return "redirect:/pedidos?error=No tienes permisos para ver este pedido";
            }

            model.addAttribute("pedido", orden);
            return "pedido-detalle"; // ← ¡CORREGIDO!

        } catch (Exception e) {
            return "redirect:/pedidos?error=Error al cargar el pedido: " + e.getMessage();
        }
    }

    @PostMapping("/pedidos/cancelar")
    public String cancelarPedido(@RequestParam Long pedidoId, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) return "redirect:/login";

        try {
            String email = principal.getName();
            Orden orden = ordenRepository.findById(pedidoId)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            if (orden.getUsuario().getEmail().equals(email)) {
                if ("PENDIENTE".equals(orden.getEstado())) {
                    orden.setEstado("CANCELADO");
                    ordenRepository.save(orden);
                    redirectAttributes.addFlashAttribute("success", "El pedido ha sido cancelado correctamente.");
                } else {
                    redirectAttributes.addFlashAttribute("error", "No se puede cancelar este pedido porque ya fue procesado.");
                }
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cancelar el pedido.");
        }

        return "redirect:/pedidos/" + pedidoId;
    }

    @PostMapping("/pedidos/archivar")
    public String archivarPedido(@RequestParam Long pedidoId, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) return "redirect:/login";

        try {
            String email = principal.getName();
            Orden orden = ordenRepository.findById(pedidoId)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            if (orden.getUsuario().getEmail().equals(email)) {
                orden.setArchivado(true);
                ordenRepository.save(orden);
                redirectAttributes.addFlashAttribute("success", "El pedido se ha archivado y ocultado de la lista.");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al archivar el pedido.");
        }

        return "redirect:/pedidos";
    }

    @PostMapping("/checkout")
    public String procesarPago(Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) return "redirect:/login";

        try {
            Orden ordenGuardada = checkoutService.procesarPedido(principal);

            if (ordenGuardada.getEstado() == null) {
                ordenGuardada.setEstado("PENDIENTE");
                ordenRepository.save(ordenGuardada);
            }

            redirectAttributes.addFlashAttribute("success", "¡Gracias! Tu pedido #" + ordenGuardada.getId() + " ha sido procesado.");
            return "redirect:/pedidos";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error en el pedido: " + e.getMessage());
            return "redirect:/cart";
        }
    }
}