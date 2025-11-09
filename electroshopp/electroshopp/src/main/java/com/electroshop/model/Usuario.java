package com.electroshop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios") // Define el nombre de la tabla en MySQL
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email; // Usaremos el email como 'username'

    @Column(nullable = false, length = 255)
    private String nombre;

    @Column(nullable = false, length = 60) // 60 caracteres para la contraseña encriptada (BCrypt)
    private String password;

    @Column(nullable = false, length = 20)
    private String role; // Almacenará "ROLE_USER" o "ROLE_ADMIN"

    // --- Constructores ---
    public Usuario() {
    }

    public Usuario(String email, String nombre, String password, String role) {
        this.email = email;
        this.nombre = nombre;
        this.password = password;
        this.role = role;
    }

    // --- Getters y Setters ---
    // (Puedes generarlos automáticamente en tu IDE o copiarlos)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
