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

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {
        cargarCategoriasEjemplo();
        cargarProductosEjemplo();
    }

    // FUNCIÓN PARA CARGAR LAS 8 CATEGORÍAS (Antiduplicados)
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
            // Utilizamos findByNombre para evitar duplicados
            if (categoriaRepository.findByNombre(nombre) == null) {
                categoriaRepository.save(new Categoria(nombre));
            }
        }
    }


    // FUNCIÓN PARA CARGAR 2 PRODUCTOS POPULARES POR CADA CATEGORÍA
    private void cargarProductosEjemplo() {
        // Limpiamos los productos viejos.
        productoRepository.deleteAll();

        System.out.println("Cargando 16 productos con nombres populares y reales...");

        // 1. Obtener TODAS las categorías cargadas
        List<Categoria> categorias = categoriaRepository.findAll();

        if (categorias.isEmpty()) {
            System.err.println("ERROR: No se encontraron categorías para cargar productos.");
            return;
        }

        System.out.println("DEBUG: Se encontraron " + categorias.size() + " categorías para poblar.");

        // imageCounter irá de 1 a 16 para las rutas locales /images/prod-X.jpg
        long imageCounter = 1;

        for (Categoria categoria : categorias) {
            String catNombre = categoria.getNombre();

            String nombreBasico;
            String descBasica;
            String nombrePremium;
            String descPremium;

            // --- LÓGICA DE ASIGNACIÓN DE NOMBRES Y DESCRIPCIONES REALES ---

            if (catNombre.equals("Generación y suministro de energía")) {
                nombreBasico = "Generador de Gasolina Portátil 1500W";
                descBasica = "Unidad compacta de respaldo, ideal para el hogar o camping. Arranque manual y tanque de 4 litros.";
                nombrePremium = "Panel Solar Monocristalino 100W";
                descPremium = "Alta eficiencia para sistemas aislados o interconexión a red. Certificación IP67 y marco de aluminio.";

            } else if (catNombre.equals("Iluminación")) {
                nombreBasico = "Foco LED A19 Luz Blanca 10W";
                descBasica = "Foco LED de uso diario, equivalente a 60W incandescente. Base E27 estándar. 15,000 horas de vida.";
                nombrePremium = "Tira LED RGB (5m) Controlable Wi-Fi";
                descPremium = "Iluminación ambiental y decorativa, control total por aplicación móvil. 16 millones de colores y modos dinámicos.";

            } else if (catNombre.equals("Conductores y accesorios de cableado")) {
                nombreBasico = "Cable Eléctrico THW Calibre 12 (Rollo 100m)";
                descBasica = "Cable de cobre suave con aislamiento termoplástico. Soporta 90°C. Color negro, para uso residencial.";
                nombrePremium = "Canaleta Adhesiva PVC (2m)";
                descPremium = "Sistema de gestión de cables de fácil instalación, ideal para cableado expuesto en oficinas o casas. Adhesivo 3M.";

            } else if (catNombre.equals("Aparatos de protección y control")) {
                nombreBasico = "Interruptor Termomagnético 1 Polos (20A)";
                descBasica = "Breaker de seguridad esencial para protección de circuitos en centros de carga residenciales, con certificación NOM.";
                nombrePremium = "Supresor de Picos de Voltaje con USB";
                descPremium = "Protección avanzada contra sobretensiones para equipo sensible (PC, TV, consolas). 6 tomas y 2 puertos USB.";

            } else if (catNombre.equals("Herramientas y accesorios eléctricos")) {
                nombreBasico = "Multímetro Digital Básico (Volts/Ohms)";
                descBasica = "Herramienta portátil y compacta para pruebas de voltaje DC/AC y continuidad. Incluye puntas de prueba.";
                nombrePremium = "Pinza Amperimétrica TRMS Profesional";
                descPremium = "Medición de corriente sin contacto. Con verdadero valor eficaz (TRMS) para máxima precisión en entornos ruidosos.";

            } else if (catNombre.equals("Instalaciones y accesorios domésticos")) {
                nombreBasico = "Contacto Doble Polarizado 15A";
                descBasica = "Placa de pared estándar con dos receptáculos, ideal para la mayoría de los aparatos domésticos. Fácil instalación.";
                nombrePremium = "Apagador Atenuador (Dimmer) Wi-Fi";
                descPremium = "Regula la intensidad de la luz y programa horarios desde tu smartphone. Compatible con focos LED dimeables.";

            } else if (catNombre.equals("Motores y equipos electromecánicos")) {
                nombreBasico = "Bomba de Agua Periférica (1/2 HP)";
                descBasica = "Bomba compacta para aumento de presión en sistemas domésticos y riego de jardines. Motor silencioso.";
                nombrePremium = "Motor Eléctrico Monofásico (1 HP)";
                descPremium = "Motor de propósito general con carcasa de hierro fundido, apto para compresores y sierras industriales.";

            } else if (catNombre.equals("Electrónica y automatización")) {
                nombreBasico = "Sensor de Movimiento Infrarrojo PIR";
                descBasica = "Dispositivo para encendido automático de luces o alarmas. Ángulo de detección de 120 grados.";
                nombrePremium = "Controlador Lógico Programable (PLC) Básico";
                descPremium = "Unidad de control compacta para automatización de procesos. Incluye 8 entradas y 4 salidas digitales.";

            } else {
                // Fallback genérico de seguridad
                nombreBasico = catNombre + " - Producto Genérico";
                descBasica = "Descripción de prueba para asegurar que el catálogo esté lleno.";
                nombrePremium = catNombre + " - Producto Genérico Premium";
                descPremium = "Descripción de prueba para asegurar que el catálogo esté lleno.";
            }

            // --- Producto 1: BÁSICO ---
            Producto p1 = new Producto();
            p1.setNombre(nombreBasico);
            p1.setModelo(catNombre.substring(0, 3).toUpperCase() + "-" + categoria.getId() + "B");
            p1.setDescripcion(descBasica);
            p1.setPrecio(new BigDecimal("100.00").add(new BigDecimal(categoria.getId() * 10)));
            p1.setStock(50);
            p1.setImagenUrl("/images/prod-" + (imageCounter++) + ".jpg");
            p1.setCategoria(categoria);
            productoRepository.save(p1);

            // --- Producto 2: PREMIUM ---
            Producto p2 = new Producto();
            p2.setNombre(nombrePremium);
            p2.setModelo(catNombre.substring(0, 3).toUpperCase() + "-" + categoria.getId() + "P");
            p2.setDescripcion(descPremium);
            p2.setPrecio(new BigDecimal("350.00").add(new BigDecimal(categoria.getId() * 20)));
            p2.setStock(30);
            p2.setImagenUrl("/images/prod-" + (imageCounter++) + ".jpg");
            p2.setCategoria(categoria);
            productoRepository.save(p2);

            System.out.println(" -> Añadidos 2 productos a: " + catNombre);
        }
        System.out.println("Carga masiva de 16 productos completada.");
    }
}

