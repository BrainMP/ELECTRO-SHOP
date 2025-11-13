package com.electroshop.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "orden_detalles")
public class OrdenDetalle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con la Orden (Muchas líneas de detalle pertenecen a Una orden)
    @ManyToOne
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    // Guardamos los datos del producto como una "foto" (snapshot)
    // No enlazamos a Producto, por si el precio o nombre del producto cambia en el futuro.

    @Column(nullable = false)
    private String nombreProducto;

    @Column(nullable = false)
    private String modeloProducto;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private BigDecimal subtotal; // (precioUnitario * cantidad)


    // --- Constructores ---
    public OrdenDetalle() {
    }

    // --- Getters y Setters ---
    // (Puedes generarlos automáticamente en tu IDE si lo prefieres)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getModeloProducto() {
        return modeloProducto;
    }

    public void setModeloProducto(String modeloProducto) {
        this.modeloProducto = modeloProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

        // --- CAMPO NUEVO ---
        @Column(nullable = true) // (Lo ponemos opcional por si acaso)
        private String imagenUrl;


        // --- GETTER Y SETTER NUEVOS ---
        public String getImagenUrl() {
            return imagenUrl;
        }

        public void setImagenUrl(String imagenUrl) {
            this.imagenUrl = imagenUrl;
        }
    }

