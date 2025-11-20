package com.electroshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoPageController {

    // === SECCIÓN ELECTRO SHOP ===

    @GetMapping("/acerca")
    public String mostrarAcerca(Model model) {
        model.addAttribute("pageTitle", "Acerca de Nosotros");
        model.addAttribute("pageHeader", "Acerca de Nosotros");
        model.addAttribute("pageContent", "Somos ELECTRO SHOP, una empresa 100% mexicana dedicada a proveer las mejores soluciones en material eléctrico, iluminación y tecnología.");
        model.addAttribute("pageMision","Misión");
        model.addAttribute("pageContent1","Ser el principal proveedor de productos electrónicos básicos, ofreciendo una selección curada de soluciones accesibles y confiables que simplifiquen la vida diaria de nuestros clientes, garantizando siempre una experiencia de compra transparente y un servicio de excelencia.");
        model.addAttribute("pageVision", "Visión");
        model.addAttribute("pageContent2","Convertirnos en la plataforma de comercio electrónico líder y de mayor confianza en la venta de electrónica básica en la región, reconocida por nuestra integridad, excelencia operativa y por hacer que la tecnología esencial sea fiable y accesible para cada hogar.");
        return "info-page";
    }

    @GetMapping("/tiendas")
    public String mostrarTiendas(Model model) {
        model.addAttribute("pageTitle", "Ubicación de Tiendas");
        model.addAttribute("pageHeader", "Ubicación de Tiendas");
        model.addAttribute("pageContent", "Actualmente operamos únicamente a través de nuestro almacén central en Predio Tetenco, Colonia Centro, en el pueblo de San Miguel Topilejo, Tlalpan,. ¡Próximamente abriremos sucursales físicas!");
        return "info-page";
    }

    @GetMapping("/catalogo-digital")
    public String mostrarCatalogo(Model model) {
        model.addAttribute("pageTitle", "Catálogo Digital");
            return "catalogo-digital";
    }

    @GetMapping("/empleo")
    public String mostrarEmpleo(Model model) {
        model.addAttribute("pageTitle", "Únete al equipo");
        model.addAttribute("pageHeader", "Únete al equipo");
        model.addAttribute("pageContent", "ELECTRO SHOP está creciendo. Si te apasiona la tecnología y el servicio al cliente, envíanos tu CV. (Contenido de Recursos Humanos iría aquí).");
        return "info-page";
    }

    // === SECCIÓN POLÍTICAS (PÁGINAS COMPLEJAS) ===

    @GetMapping("/terminos")
    public String mostrarTerminos(Model model) {
        model.addAttribute("pageTitle", "Términos y Condiciones");
        // Ya no enviamos "pageContent"
        return "terminos"; // <-- AHORA APUNTA A "terminos.html"
    }

    @GetMapping("/privacidad")
    public String mostrarPrivacidad(Model model) {
        model.addAttribute("pageTitle", "Política de Privacidad");
        // Ya no enviamos "pageContent"
        return "privacidad"; // <-- AHORA APUNTA A "privacidad.html"
    }

    @GetMapping("/garantias")
    public String mostrarGarantias(Model model) {
        model.addAttribute("pageTitle", "Garantías");
        // Ya no enviamos "pageContent" ni "PageGarant"
        return "garantias"; // <-- AHORA APUNTA A "garantias.html"
    }
}