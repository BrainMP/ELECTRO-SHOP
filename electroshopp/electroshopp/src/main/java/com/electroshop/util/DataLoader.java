package com.electroshop.util;

import com.electroshop.model.Categoria;
import com.electroshop.model.Producto;
import com.electroshop.repository.CategoriaRepository;
import com.electroshop.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.Arrays; // Necesario para la carga de categorías, si la haces aquí
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Asegúrate de que este método también se ejecute para cargar categorías.
        // Si tienes el código de carga de categorías en otro lugar, omite esta parte.
        cargarCategoriasEjemplo();

        cargarProductosEjemplo();
    }

    // NOTA: ASUMO QUE TIENES ESTE MÉTODO PARA CARGAR TUS 8 CATEGORÍAS
    private void cargarCategoriasEjemplo() {
        List<String> nombresCategorias = Arrays.asList(
                "Generación y suministro de energía", "Iluminación",
                "Conductores y accesorios de cableado", "Aparatos de protección y control",
                "Herramientas y accesorios eléctricos", "Instalaciones y accesorios domésticos",
                "Motores y equipos electromecánicos", "Electrónica y automatización"
        );
        for (String nombre : nombresCategorias) {
            if (categoriaRepository.findByNombre(nombre) == null) {
                categoriaRepository.save(new Categoria(nombre));
            }
        }
    }


    // ------------------------------------------------------------------
    // NUEVO MÉTODO PARA CARGAR PRODUCTOS (CON LIMPIEZA)
    // ------------------------------------------------------------------
    private void cargarProductosEjemplo() {
        System.out.println("Cargando productos de ejemplo...");

        // 🛑 SOLUCIÓN AL PROBLEMA DE DUPLICADOS: Limpiar la tabla de productos
        productoRepository.deleteAll();

        // 1. OBTENER LAS CATEGORÍAS (necesarias para la relación)
        Categoria iluminacion = categoriaRepository.findByNombre("Iluminación");
        Categoria instalaciones = categoriaRepository.findByNombre("Instalaciones y accesorios domésticos");
        Categoria conductores = categoriaRepository.findByNombre("Conductores y accesorios de cableado");

        // Solo continuamos si las categorías principales existen
        if (iluminacion != null && instalaciones != null && conductores != null) {

            // 2. CREAR Y GUARDAR PRODUCTOS
            // Producto 1: Foco Wi-Fi
            Producto p1 = new Producto();
            p1.setNombre("Foco LED Wi-Fi RGB+W multicolor de 10 W");
            p1.setModelo("SHOME 120");
            p1.setDescripcion("Foco inteligente que se controla por app y cambia de color.");
            p1.setPrecio(new BigDecimal("199.50"));
            p1.setStock(50);
            p1.setImagenUrl("https://via.placeholder.com/200x200?text=FOCO+RGB+10W");
            p1.setCategoria(iluminacion);
            productoRepository.save(p1);

            // Producto 2: Contacto Wi-Fi
            Producto p2 = new Producto();
            p2.setNombre("Contacto Wi-Fi");
            p2.setModelo("SHOME 100");
            p2.setDescripcion("Convierte cualquier aparato en inteligente con este contacto.");
            p2.setPrecio(new BigDecimal("125.00"));
            p2.setStock(120);
            p2.setImagenUrl("https://via.placeholder.com/200x200?text=CONTACTO+WIFI");
            p2.setCategoria(instalaciones);
            productoRepository.save(p2);

            // Producto 3: Tira LED Neón
            Producto p3 = new Producto();
            p3.setNombre("Tira LED Neón Wi-Fi multicolor de 5 metros");
            p3.setModelo("SHOME 1290D");
            p3.setDescripcion("Tira flexible para decoración exterior o interior.");
            p3.setPrecio(new BigDecimal("599.99"));
            p3.setStock(30);
            p3.setImagenUrl("https://via.placeholder.com/200x200?text=TIRA+LED+NEON");
            p3.setCategoria(iluminacion);
            productoRepository.save(p3);

            // Producto 4: Cable de Potencia
            Producto p4 = new Producto();
            p4.setNombre("Cable de Potencia THW, Calibre 12, Rojo (50m)");
            p4.setModelo("CBL-12R");
            p4.setDescripcion("Cable de cobre puro para instalaciones de alta demanda.");
            p4.setPrecio(new BigDecimal("980.00"));
            p4.setStock(75);
            p4.setImagenUrl("https://via.placeholder.com/200x200?text=CABLE+ROJO");
            p4.setCategoria(conductores);
            productoRepository.save(p4);

            // Duplicamos un producto para simular más elementos en el catálogo
            Producto p5 = new Producto();
            p5.setNombre("Multímetro Digital PRO");
            p5.setModelo("MULTI-005");
            p5.setDescripcion("Herramienta de precisión para mediciones eléctricas.");
            p5.setPrecio(new BigDecimal("450.00"));
            p5.setStock(40);
            p5.setImagenUrl("https://via.placeholder.com/200x200?text=MULTIMETRO");
            // Usamos una categoría diferente si existe, si no, usa una de las disponibles
            Categoria herramientas = categoriaRepository.findByNombre("Herramientas y accesorios eléctricos");
            p5.setCategoria(herramientas != null ? herramientas : iluminacion);
            productoRepository.save(p5);

            System.out.println("Productos de ejemplo cargados con éxito.");
        } else {
            System.err.println("ERROR: No se encontraron categorías clave para cargar productos.");
        }
    }
}

