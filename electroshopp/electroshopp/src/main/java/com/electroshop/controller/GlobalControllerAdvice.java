package com.electroshop.controller;

import com.electroshop.model.Categoria;
import com.electroshop.model.Usuario; // (1) IMPORTAR MODELO USUARIO
import com.electroshop.repository.CategoriaRepository;
import com.electroshop.repository.UsuarioRepository; // (2) IMPORTAR REPOSITORIO USUARIO
import com.electroshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal; // (3) IMPORTAR PRINCIPAL (para saber quién está logueado)
import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private UsuarioRepository usuarioRepository; // (4) INYECTAR REPOSITORIO USUARIO

    @ModelAttribute
    public void addGlobalAttributes(Model model, Principal principal) { // (5) AÑADIR PRINCIPAL

        // Cargar categorías (esto ya lo tenías)
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("categoriasGlobal", categorias);

        // Cargar contador del carrito (esto ya lo tenías)
        model.addAttribute("cartItemCount", cartService.getTotalQuantity());


        // (6) NUEVA LÓGICA: BUSCAR EL NOMBRE DEL USUARIO

        if (principal != null) {
            // Si el usuario está logueado (principal no es null)
            String email = principal.getName(); // Obtenemos el email (username)

            // Buscamos el usuario en la BD
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

            if (usuarioOpt.isPresent()) {
                // Si lo encontramos, añadimos su nombre al modelo
                model.addAttribute("usuarioNombre", usuarioOpt.get().getNombre());
            } else {
                // Si por alguna razón no está en la BD, mostramos el email
                model.addAttribute("usuarioNombre", email);
            }
        }
        // Si principal es null (anónimo), no se añade "usuarioNombre"
    }
}