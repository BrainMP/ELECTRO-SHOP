package com.electroshop.controller;

import com.electroshop.model.Producto;
import com.electroshop.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // 1. Método para la Búsqueda de Texto (Ruta: /)
    @GetMapping("/")
    public String verCatalogo(
            @RequestParam(value = "q", required = false) String query,
            Model model
    ) {
        List<Producto> productos;

        if (query != null && !query.isEmpty()) {
            // Caso 1: Búsqueda activa (por texto)
            productos = productoRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(query, query);
            model.addAttribute("titulo", "Resultados para: " + query);
        } else {
            // Caso 2: Vista de catálogo completa
            productos = productoRepository.findAll();
            model.addAttribute("titulo", "Catálogo");
        }

        model.addAttribute("productos", productos);

        // ¡Se recomienda añadir el título al modelo, incluso si no lo usas en HTML aún!
        // model.addAttribute("titulo", "Título de página");

        return "catalogo";
    }

    // 2. Método para el Filtrado por Categoría (Ruta: /catalogo?cat=Nombre)
    @GetMapping("/catalogo")
    public String verCatalogoPorCategoria(
            @RequestParam("cat") String nombreCategoria,
            Model model
    ) {

        // Lógica: Filtra los productos usando el nuevo método del repositorio
        List<Producto> productos = productoRepository.findByCategoria_Nombre(nombreCategoria);

        // Enviamos el título de la categoría
        model.addAttribute("titulo", "Categoría: " + nombreCategoria);

        // Enviamos los productos filtrados
        model.addAttribute("productos", productos);

        // Reutilizamos la misma plantilla de catálogo
        return "catalogo";
    }

    // ... dentro de la clase ProductoController ...

    // Mapea la URL /producto/{id} para ver el detalle de un artículo
    @GetMapping("/producto/{id}")
    public String verDetalleProducto(
            @PathVariable Long id, // Captura el {id} de la URL
            Model model
    ) {

        // 1. Buscar el producto por su ID.
        // .orElseThrow() maneja el caso de que el ID no exista, lanzando un error 404 (o 500)
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        // 2. Envía el objeto 'producto' completo a la vista
        model.addAttribute("producto", producto);
        model.addAttribute("titulo", producto.getNombre());

        // 3. Devolver la plantilla HTML (detalle-producto.html)
        return "detalle-producto";
    }

}