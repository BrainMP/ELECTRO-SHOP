package com.electroshop.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes") // Así se llamará la tabla en MySQL
public class Orden implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private BigDecimal montoTotal;

    // =================================================================
    // NUEVOS CAMPOS PARA LAS MEJORAS (ESTADO Y ARCHIVADO)
    // =================================================================

    // Para saber si se puede cancelar (PENDIENTE) o si ya finalizó
    @Column(length = 20)
    private String estado;

    // Para la función "Ocultar/Archivar" pedido de la lista principal
    private boolean archivado = false;

    // =================================================================

    /**
     * Relación con el Usuario (Muchos pedidos pertenecen a Un usuario)
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Relación con los Detalles (Una orden tiene Muchas líneas de detalle)
     */
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrdenDetalle> detalles = new ArrayList<>();


    // --- Constructores ---
    public Orden() {
        this.fechaCreacion = LocalDateTime.now();

        // CAMBIO: Inicializamos el estado por defecto al crear una orden
        this.estado = "PENDIENTE";
    }

    // --- Getters y Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<OrdenDetalle> getDetalles() { return detalles; }
    public void setDetalles(List<OrdenDetalle> detalles) { this.detalles = detalles; }

    // --- NUEVOS GETTERS Y SETTERS NECESARIOS ---

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isArchivado() {
        return archivado;
    }

    public void setArchivado(boolean archivado) {
        this.archivado = archivado;
    }

    // --- Método útil ---
    public void addDetalle(OrdenDetalle detalle) {
        detalles.add(detalle);
        detalle.setOrden(this);
    }
}