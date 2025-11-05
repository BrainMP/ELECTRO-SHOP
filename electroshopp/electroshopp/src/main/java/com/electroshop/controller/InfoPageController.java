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
        model.addAttribute("pageContent", "Actualmente operamos únicamente a través de nuestro almacén central en la Ciudad de México. ¡Próximamente abriremos sucursales físicas!");
        return "info-page";
    }

    @GetMapping("/catalogo-digital")
    public String mostrarCatalogo(Model model) {
        model.addAttribute("pageTitle", "Catálogo Digital");
        model.addAttribute("pageHeader", "Catálogo Digital");
        model.addAttribute("pageContent", "Explora nuestro catálogo digital interactivo. (Esta sección podría eventualmente mostrar un visor de PDF o un enlace a un servicio externo).");
        return "info-page";
    }

    @GetMapping("/empleo")
    public String mostrarEmpleo(Model model) {
        model.addAttribute("pageTitle", "Únete al equipo");
        model.addAttribute("pageHeader", "Únete al equipo");
        model.addAttribute("pageContent", "ELECTRO SHOP está creciendo. Si te apasiona la tecnología y el servicio al cliente, envíanos tu CV. (Contenido de Recursos Humanos iría aquí).");
        return "info-page";
    }

    // === SECCIÓN POLÍTICAS ===
    @GetMapping("/terminos")
    public String mostrarTerminos(Model model) {
        model.addAttribute("pageTitle", "Términos y Condiciones");
        model.addAttribute("pageHeader", "Términos y Condiciones");
        model.addAttribute("pageContent", "Nuestros Términos y Condiciones");
        return "info-page";
    }

    @GetMapping("/privacidad")
    public String mostrarPrivacidad(Model model) {
        model.addAttribute("pageTitle", "Política de Privacidad");
        model.addAttribute("pageHeader", "Aviso de Privacidad");
        model.addAttribute("pageContent", "En ELECTRO SHOP, la privacidad de tu información es primordial. Este aviso detalla cómo recolectamos, usamos y protegemos tus datos personales en cumplimiento con la Ley Federal de Protección de Datos...");
        return "info-page";
    }

    @GetMapping("/garantias")
    public String mostrarGarantias(Model model) {
        model.addAttribute("pageTitle", "Garantías");
        model.addAttribute("pageHeader", "Información de Garantías");
        model.addAttribute("pageContent", "La Póliza de Garantía es el documento contractual que respalda la calidad de los productos electrónicos básicos vendidos por ElectroShop. En cumplimiento con la normativa, esta póliza debe ser clara, accesible y debe garantizar al consumidor la protección de sus derechos.");
        model.addAttribute("PageGarant","I. Alcance y Vigencia de la Garantía");
        model.addAttribute("pageContent1","1.\tVigencia Mínima Legal: Todo producto electrónico vendido por ElectroShop cuenta con una garantía mínima legal de 90 días naturales contra defectos de fabricación o fallas ocultas, contados a partir de la recepción del producto por parte del Consumidor. Cualquier extensión de este plazo será especificada en la póliza particular del producto.\n" +
                "2.\tIdentificación del Responsable: La póliza debe especificar claramente el nombre, domicilio y datos de contacto de ElectroShop, como el proveedor responsable de hacer efectiva la garantía.\n" +
                "3.\tProductos Importados: Si ElectroShop distribuye productos importados, la información proporcionada debe incluir su lugar de origen y los sitios designados para su reparación, así como las instrucciones completas para su uso.\n");
        model.addAttribute("PageGarant","Procedimiento para Hacer Válida la Garantía");
        model.addAttribute("pageContent2","Si un producto adquirido a través de ElectroShop presenta una falla durante la vigencia de la garantía, el consumidor tiene el derecho de contactar inmediatamente al proveedor responsable para solicitar la aplicación de la garantía.\n" +
                "El consumidor podrá ejercer su derecho de garantía en el domicilio en el que se adquirió el bien o en el lugar o lugares que se expresen en la propia póliza.\n");
        model.addAttribute("PageGarant","Opciones de Cumplimiento de la Garantía");
        model.addAttribute("pageContent3","Una vez confirmada la falla cubierta por la garantía, el consumidor tendrá derecho a elegir, a su entera discreción, entre las siguientes opciones, según lo establecido por la ley:\n" +
                "1.\tReparación: El producto será reparado o el servicio será corregido sin costo alguno para el consumidor. El servicio de reparación durante la vigencia de la garantía es gratuito, y el proveedor no debe aplicar cobros adicionales ni condicionar su cumplimiento. Si procede el cambio de piezas, estas deben ser sustituidas por originales.\n" +
                "2.\tSustitución: El producto defectuoso será sustituido por uno nuevo, en perfecto estado y con las mismas o mejores características.\n" +
                "3.\tDevolución del Precio: En caso de que la reparación o sustitución no sea posible, se realizará la devolución total del precio pagado por el consumidor.\n");
        model.addAttribute("PageGarant","Cobertura de Gastos Adicionales ");
        model.addAttribute("pageContent4","ElectroShop se obliga a cubrir los gastos necesarios erogados por el consumidor para lograr el cumplimiento de la garantía, incluyendo específicamente los costos de envío y traslado del producto defectuoso al centro de servicio o al domicilio de reparación.");
        model.addAttribute("PageGarant5","Política de Devoluciones y Desistimiento");
        model.addAttribute("pageContent6","Derecho de Desistimiento: ElectroShop proporcionará un formulario de desistimiento de compra y detallará los plazos y condiciones bajo los cuales se acepta la devolución del producto, de acuerdo con la Ley Federal de Protección al Consumidor para compras realizadas a distancia.");
        model.addAttribute("PageGarant","Plazos y Condiciones de Entrega ");
        model.addAttribute("pageContent7","Compromiso de Plazo: Si al momento de la compra no se indica un plazo de envío específico, ElectroShop se compromete a que la entrega del producto se realice en un máximo de 30 días naturales desde que el consumidor formalizó la transacción. La promesa de entrega estipulada deberá ser cumplida rigurosamente.");
        return "info-page";
    }
}
