package com.electroshop.util;

import com.electroshop.model.Categoria;
import com.electroshop.model.Producto;
import com.electroshop.repository.CategoriaRepository;
import com.electroshop.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Aseguramos que las categorías existan ANTES de cargar productos
        cargarCategoriasEjemplo();
        cargarProductosEjemplo();
    }

    // FUNCIÓN PARA CARGAR LAS 8 CATEGORÍAS
    private void cargarCategoriasEjemplo() {
        List<String> nombresCategorias = Arrays.asList(
                "Generación y suministro de energía",
                "Iluminación",
                "Conductores y accesorios de cableado",
                "Aparatos de protección y control",
                "Herramientas y accesorios eléctricos",
                "Instalaciones y accesorios domésticos",
                "Motores y equipos electromecánicos",
                "Electrónica y automatización"
        );
        for (String nombre : nombresCategorias) {
            // Utilizamos el método findByNombre para evitar duplicados,
            // asegurando que solo se inserten una vez.
            if (categoriaRepository.findByNombre(nombre) == null) {
                categoriaRepository.save(new Categoria(nombre));
            }
        }
    }


    // FUNCIÓN PARA CARGAR 2 PRODUCTOS POR CADA CATEGORÍA
    private void cargarProductosEjemplo() {
        // Limpiamos los productos viejos.
        productoRepository.deleteAll();

        System.out.println("Cargando 2 productos por cada categoría...");

        // 1. Obtener TODAS las categorías (Spring ya debería tener la lista completa aquí)
        List<Categoria> categorias = categoriaRepository.findAll();

        if (categorias.isEmpty()) {
            System.err.println("ERROR: No se encontraron categorías para cargar productos.");
            return;
        }

        // DEBUG: Muestra cuántas categorías encontró (deberían ser 8)
        System.out.println("DEBUG: Se encontraron " + categorias.size() + " categorías para poblar.");


        // 2. Loop a través de cada categoría e insertar 2 productos
        long imageId = 100;
        for (Categoria categoria : categorias) {
            String catNombre = categoria.getNombre();

            // --- Producto 1: BÁSICO ---
            Producto p1 = new Producto();
            p1.setNombre(catNombre + " - Modelo Básico");
            p1.setModelo(catNombre.substring(0, 3).toUpperCase() + "-" + categoria.getId() + "B");
            p1.setDescripcion("Artículo estándar y confiable de la línea de " + catNombre + ".");
            p1.setPrecio(new BigDecimal("100.00").add(new BigDecimal(categoria.getId() * 10)));
            p1.setStock(50);
            p1.setImagenUrl("https://picsum.photos/id/" + (imageId++) + "/200/200");
            p1.setCategoria(categoria);
            productoRepository.save(p1);

            // --- Producto 2: PREMIUM ---
            Producto p2 = new Producto();
            p2.setNombre(catNombre + " - Modelo Premium");
            p2.setModelo(catNombre.substring(0, 3).toUpperCase() + "-" + categoria.getId() + "P");
            p2.setDescripcion("Versión de alto rendimiento con garantía extendida para " + catNombre + ".");
            p2.setPrecio(new BigDecimal("350.00").add(new BigDecimal(categoria.getId() * 20)));
            p2.setStock(30);
            p2.setImagenUrl("https://picsum.photos/id/" + (imageId++) + "/200/200");
            p2.setCategoria(categoria);
            productoRepository.save(p2);

            System.out.println(" -> Añadidos 2 productos a: " + catNombre);
        }
        System.out.println("Carga masiva de 16 productos completada.");
    }
}

