package com.electroshop.controller;

import com.electroshop.model.Tarjeta;
import com.electroshop.model.Usuario;
import com.electroshop.repository.TarjetaRepository;
import com.electroshop.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // (1) INYECTAR EL REPOSITORIO DE TARJETAS
    @Autowired
    private TarjetaRepository tarjetaRepository;

    // -----------------------------------------------------------------
    // MÉTODO FALTANTE: MOSTRAR LA PÁGINA DE LOGIN
    // -----------------------------------------------------------------
    /**
     * Muestra el formulario de inicio de sesión personalizado.
     * Esto es necesario porque lo definimos en SecurityConfig (.loginPage("/login")).
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login"; // Busca el archivo templates/login.html
    }

    /**
     * Muestra la página de registro (el formulario).
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return "register"; // Busca el archivo templates/register.html
    }

    /**
     * Procesa el formulario de registro.
     */
    @PostMapping("/register")
    public String processRegistration(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password,
            RedirectAttributes redirectAttributes
    ) {

        // 1. Verificar si el email (username) ya existe en la BD
        if (usuarioRepository.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "El correo electrónico ya está registrado.");
            return "redirect:/register";
        }

        // 2. Encriptar la contraseña
        String encodedPassword = passwordEncoder.encode(password);

        // 3. Crear el nuevo usuario
        Usuario nuevoUsuario = new Usuario(email, nombre, encodedPassword, "ROLE_USER");

        // 4. Guardar el usuario en la base de datos
        usuarioRepository.save(nuevoUsuario);

        // 5. Redirigir al Login con un mensaje de éxito
        redirectAttributes.addFlashAttribute("success", "¡Cuenta creada con éxito! Por favor, inicia sesión.");
        return "redirect:/login";
    }

    // -----------------------------------------------------------------
    // MÉTODO MODIFICADO: MOSTRAR LA PÁGINA DE PERFIL Y CARGAR TARJETAS (Paso 6)
    // -----------------------------------------------------------------
    @GetMapping("/perfil")
    public String showProfilePage(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login"; // Proteger la ruta si no hay sesión
        }

        // (1) Obtener el objeto Usuario completo
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // (2) Buscar y pasar las tarjetas guardadas
        List<Tarjeta> tarjetas = tarjetaRepository.findByUsuario(usuario);

        // (3) Enviar datos a la vista
        model.addAttribute("usuario", usuario); // Usuario principal
        model.addAttribute("tarjetas", tarjetas); // Lista de tarjetas

        return "perfil"; // Busca el archivo templates/perfil.html
    }

    // -----------------------------------------------------------------
    // MÉTODO: PROCESAR LA ACTUALIZACIÓN DEL PERFIL
    // -----------------------------------------------------------------
    @PostMapping("/perfil/actualizar")
    public String updateProfile(
            @RequestParam String nombre,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        String email = principal.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // (1) Actualizar el nombre
            usuario.setNombre(nombre);

            // (2) Guardar los cambios en la BD
            usuarioRepository.save(usuario);

            redirectAttributes.addFlashAttribute("success", "¡Perfil actualizado con éxito!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el perfil.");
        }

        return "redirect:/perfil"; // Redirige de vuelta a la página de perfil
    }

    // Dentro de AuthController.java, después de updateProfile(...)

    // -----------------------------------------------------------------
// MÉTODO NUEVO: PROCESAR EL CAMBIO DE EMAIL
// -----------------------------------------------------------------
    @PostMapping("/perfil/cambiar-email")
    public String updateEmail(
            @RequestParam String nuevoEmail,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        if (principal == null) {
            return "redirect:/login";
        }

        String emailActual = principal.getName();

        // (1) Verificar si el nuevo email ya está en uso por otro usuario
        if (!emailActual.equals(nuevoEmail) && usuarioRepository.findByEmail(nuevoEmail).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Ese correo ya está registrado por otra cuenta.");
            return "redirect:/perfil";
        }

        // (2) Obtener y actualizar el usuario
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(emailActual);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEmail(nuevoEmail); // Actualiza el email
            usuarioRepository.save(usuario);

            // Cierra la sesión forzando la redirección al logout de Spring Security
            redirectAttributes.addFlashAttribute("success", "Correo actualizado con éxito. Por favor, vuelve a iniciar sesión con tu nuevo correo.");
            return "redirect:/logout";
        } else {
            redirectAttributes.addFlashAttribute("error", "Error interno al encontrar su perfil.");
            return "redirect:/perfil";
        }
    }

}
