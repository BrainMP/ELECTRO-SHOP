package com.electroshop.model;

import jakarta.persistence.*; // Asegúrate de tener todos los imports de 'jakarta.persistence'

import java.io.Serializable;

@Entity
@Table(name = "categorias")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;


    // ¡LA CORRECCIÓN ESTÁ AQUÍ!

    @Id // 1. Marca este campo como la LLAVE PRIMARIA
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 2. Le dice a MySQL que genere el valor automáticamente (AUTO_INCREMENT)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    private String descripcion;

    // --- Constructores, Getters y Setters ---

    public Categoria() {
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    // ¡Asegúrate de que los Getters y Setters existan!
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    // ... otros getters/setters
}