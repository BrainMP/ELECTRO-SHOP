package com.electroshop.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.io.Serializable;
// Import necesario para @GeneratedValue(strategy = GenerationType.IDENTITY)
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "productos")
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- CAMPOS DE DATOS ---
    @Column(nullable = false, length = 200)
    private String nombre;

    private String modelo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private BigDecimal precio;

    private Integer stock;

    private String imagenUrl;

    // --- RELACIÓN CON CATEGORIA ---
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;


    // --- CONSTRUCTOR (Obligatorio para JPA) ---
    public Producto() {}


    // --- MÉTODOS GETTERS (CRUCIALES PARA THYMELEAF) ---

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getModelo() { // ¡Getter necesario!
        return modelo;
    }

    public String getDescripcion() { // ¡Getter necesario!
        return descripcion;
    }

    public BigDecimal getPrecio() { // ¡Getter necesario!
        return precio;
    }

    public Integer getStock() { // ¡Getter necesario!
        return stock;
    }

    public String getImagenUrl() { // ¡Getter CRUCIAL para el error actual!
        return imagenUrl;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    // --- MÉTODOS SETTERS (CRUCIALES PARA DATALOADER) ---

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}