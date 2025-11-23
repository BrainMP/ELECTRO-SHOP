package com.electroshop.config;

// 1. IMPORTAR EL SERVICIO DE USUARIOS
import com.electroshop.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 2. INYECTAR EL SERVICIO
    // Spring Security necesita saber CÓMO encontrar a los usuarios.
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // 3. RUTAS PÚBLICAS (Incluyendo /login y /register)
                        .requestMatchers(
                                "/", "/login", "/register",
                                "/catalogo/**", "/producto/**","terminos",
                                "/acerca", "/privacidad", "/garantias", "/tiendas",
                                "/catalogo-digital", "/empleo", "/contrato", "/etica",
                                "/css/**", "/js/**", "/images/**"
                        ).permitAll()

                        // 4. RUTAS PRIVADAS
                        .requestMatchers("/cart/**").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/", true) // A dónde ir después de un login exitoso
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // 5. CONECTAR EL SERVICIO
                // Le decimos a Spring Security que use nuestro "buscador" de usuarios.
                .userDetailsService(userDetailsService);

        return http.build();
    }
}