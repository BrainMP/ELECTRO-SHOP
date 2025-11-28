package com.electroshop.controller;

import com.electroshop.model.Tarjeta;
import com.electroshop.model.Usuario;
import com.electroshop.repository.TarjetaRepository;
import com.electroshop.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Random;

@Controller
public class TarjetaController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TarjetaRepository tarjetaRepository;

    /**
     * Helper para obtener el usuario logueado.
     */
    private Usuario getUsuarioLogueado(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Usuario no autenticado.");
        }
        String email = principal.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    }


    // ----------------------------------------------------
    // MOSTRAR FORMULARIO (GET)
    // ----------------------------------------------------
    @GetMapping("/perfil/tarjetas/agregar")
    public String showAddCardForm(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Proteger la ruta
        }

        // Muestra el formulario tarjeta_add.html
        return "tarjeta-add";
    }

    // ----------------------------------------------------
    // PROCESAR GUARDADO (POST)
    // ----------------------------------------------------
    @PostMapping("/perfil/tarjetas/guardar")
    public String saveCard(
            @RequestParam String numero,
            @RequestParam String vencimiento,
            @RequestParam String titular,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Usuario usuario = getUsuarioLogueado(principal);

            // --- Lógica de Simulación de Pago ---

            // 1. Validar Mínimo (Simulación simple de un número de 16 dígitos)
            if (numero.length() < 12) {
                throw new IllegalArgumentException("El número de tarjeta es demasiado corto.");
            }

            // 2. Determinar Tipo (Simulación simple: VISA o MASTERCARD)
            String tipo = determinarTipoTarjeta(numero);

            // 3. Crear Máscara y Token Simulado
            String digitosFinales = numero.substring(numero.length() - 4);
            String token = generarToken(); // Genera un ID único como si viniera del banco

            // 4. Crear y guardar la Tarjeta
            Tarjeta nuevaTarjeta = new Tarjeta();
            nuevaTarjeta.setUsuario(usuario);
            nuevaTarjeta.setTipo(tipo);
            nuevaTarjeta.setDigitosFinales(digitosFinales);
            nuevaTarjeta.setFechaVencimiento(vencimiento);
            nuevaTarjeta.setTokenSimulado(token);

            tarjetaRepository.save(nuevaTarjeta);

            redirectAttributes.addFlashAttribute("success", "Tarjeta " + tipo + " terminada en " + digitosFinales + " guardada con éxito.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
            return "redirect:/perfil/tarjetas/agregar";
        }

        return "redirect:/perfil"; // Redirige a la vista principal del perfil
    }

    // --- Métodos Auxiliares de Simulación ---

    private String determinarTipoTarjeta(String numero) {
        if (numero.startsWith("4")) {
            return "VISA";
        }
        if (numero.startsWith("5")) {
            return "MASTERCARD";
        }
        return "CARNET";
    }

    private String generarToken() {
        // Simulación de un token de 10 caracteres
        return String.valueOf(new Random().nextLong()).substring(0, 10);
    }

    // ----------------------------------------------------
// ELIMINAR TARJETA (POST)
// ----------------------------------------------------
    @PostMapping("/perfil/tarjetas/eliminar")
    public String deleteCard(
            @RequestParam("tarjetaId") Long tarjetaId,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            Usuario usuario = getUsuarioLogueado(principal);

            // 1. Buscar la tarjeta por ID
            Tarjeta tarjeta = tarjetaRepository.findById(tarjetaId)
                    .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

            // 2. SEGURIDAD CRÍTICA: Verificar que la tarjeta pertenezca al usuario actual
            if (!tarjeta.getUsuario().getId().equals(usuario.getId())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para eliminar esta tarjeta.");
                return "redirect:/perfil";
            }

            // 3. Eliminar la tarjeta
            tarjetaRepository.delete(tarjeta);
            redirectAttributes.addFlashAttribute("success", "Tarjeta eliminada correctamente.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la tarjeta.");
        }

        return "redirect:/perfil";
    }
}
