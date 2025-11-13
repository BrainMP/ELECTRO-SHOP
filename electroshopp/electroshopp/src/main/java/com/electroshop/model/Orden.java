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

    // --- Relaciones ---

    /**
     * Relación con el Usuario (Muchos pedidos pertenecen a Un usuario)
     * Cuando se carga una Orden, también se trae la info del Usuario.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Relación con los Detalles (Una orden tiene Muchas líneas de detalle)
     * CascadeType.ALL: Si guardas/borras una Orden, se guardan/borran sus detalles.
     * mappedBy: Le dice a JPA que la clase 'OrdenDetalle' es la dueña de esta relación.
     */
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrdenDetalle> detalles = new ArrayList<>();


    // --- Constructores ---
    public Orden() {
        // Asigna la fecha actual al momento de crear la orden
        this.fechaCreacion = LocalDateTime.now();
    }

    // --- Getters y Setters ---
    // (Puedes generarlos automáticamente en tu IDE si lo prefieres)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<OrdenDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<OrdenDetalle> detalles) {
        this.detalles = detalles;
    }

    // --- Método útil ---
    // Sincroniza la relación: añade un detalle a la lista Y
    // le asigna esta orden al detalle.
    public void addDetalle(OrdenDetalle detalle) {
        detalles.add(detalle);
        detalle.setOrden(this);
    }
}
