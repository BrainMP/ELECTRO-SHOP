package com.electroshop.controller;

import com.electroshop.model.Usuario;
import com.electroshop.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // -----------------------------------------------------------------
    // MÉTODO FALTANTE: MOSTRAR LA PÁGINA DE LOGIN
    // -----------------------------------------------------------------
    /**
     * Muestra el formulario de inicio de sesión personalizado.
     * Esto es necesario porque lo definimos en SecurityConfig (.loginPage("/login")).
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        // (Opcional) Si tienes mensajes de éxito/error, Thymeleaf los maneja
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
    // MÉTODO NUEVO: MOSTRAR LA PÁGINA DE "EDITAR PERFIL"
    // -----------------------------------------------------------------
    @GetMapping("/perfil")
    public String showProfilePage(Model model, Principal principal) {

        // (1) Obtener el email del usuario logueado
        String email = principal.getName();

        // (2) Buscar el usuario en la base de datos
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            // Manejar error si el usuario no se encuentra (aunque debería)
            return "redirect:/login?error=Usuario no encontrado";
        }

        // (3) Enviar el objeto Usuario completo a la vista
        model.addAttribute("usuario", usuarioOpt.get());

        return "perfil"; // (4) Busca el archivo templates/perfil.html
    }

    // -----------------------------------------------------------------
    // MÉTODO NUEVO: PROCESAR LA ACTUALIZACIÓN DEL PERFIL
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
}
