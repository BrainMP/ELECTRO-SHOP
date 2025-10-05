package com.electroshop.controller;

import com.electroshop.model.Categoria;
import com.electroshop.repository.CategoriaRepository;
import com.electroshop.service.CartService; // Necesario para contar ítems
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired // Inyectamos el servicio de sesión
    private CartService cartService;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        // 1. Cargar categorías para el header/footer
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("categoriasGlobal", categorias);

        // 2. Cargar el contador del carrito desde la sesión (RESUELVE 1, 2, 4)
        model.addAttribute("cartItemCount", cartService.getTotalQuantity());
    }
}
