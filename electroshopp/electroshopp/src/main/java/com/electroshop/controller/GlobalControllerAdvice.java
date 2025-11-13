package com.electroshop.controller;

import com.electroshop.model.Categoria;
import com.electroshop.model.Usuario;
import com.electroshop.repository.CategoriaRepository;
import com.electroshop.repository.UsuarioRepository;
import com.electroshop.service.CartService; // Asegúrate de importar el CartService v2
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal; // (1) IMPORTAR PRINCIPAL
import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @ModelAttribute
    public void addGlobalAttributes(Model model, Principal principal) { // (2) INYECTAR PRINCIPAL

        // Cargar categorías (esto se mantiene igual)
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("categoriasGlobal", categorias);

        String email = null;
        if (principal != null) {
            email = principal.getName(); // Obtenemos el email si está logueado
        }

        // ----------------------------------------------------
        // (3) CAMBIO CLAVE: Lógica del Contador de Carrito
        // ----------------------------------------------------
        // El contador ahora usa el email. Si el email es null (anónimo),
        // el servicio devolverá 0.
        model.addAttribute("cartItemCount", cartService.getTotalQuantity(email));

        // ----------------------------------------------------
        // (4) CAMBIO CLAVE: Lógica del Nombre de Usuario
        // ----------------------------------------------------
        if (email != null) {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
            if (usuarioOpt.isPresent()) {
                model.addAttribute("usuarioNombre", usuarioOpt.get().getNombre());
            } else {
                model.addAttribute("usuarioNombre", email); // Fallback
            }
        }
    }
}