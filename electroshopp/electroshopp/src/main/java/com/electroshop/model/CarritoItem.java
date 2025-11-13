package com.electroshop.model;

import jakarta.persistence.*;
import java.io.Serializable; // Importar Serializable

@Entity
@Table(name = "carrito_items")
public class CarritoItem implements Serializable { // Implementamos Serializable

    private static final long serialVersionUID = 2L; // ID de Versión

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación Muchos a Uno: Muchos items pertenecen a UN carrito.
     */
    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    /**
     * Relación Muchos a Uno: Muchos items pueden apuntar al MISMO producto.
     */
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private int cantidad;

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // (Opcional pero recomendado: equals y hashCode basados en ID)
}
