package com.electroshop.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tarjetas") // Nueva tabla para guardar métodos de pago
public class Tarjeta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Relación: Muchas tarjetas pertenecen a UN Usuario ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // --- Datos de la Tarjeta (Solo lo necesario para mostrar) ---

    @Column(nullable = false, length = 10)
    private String tipo; // Ejemplo: VISA, MASTERCARD

    @Column(nullable = false, length = 4)
    private String digitosFinales; // Ejemplo: 5735 (Máscara)

    @Column(nullable = false, length = 10)
    private String tokenSimulado; // Simula el token que daría el banco

    @Column(nullable = false, length = 5)
    private String fechaVencimiento; // MM/AA

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDigitosFinales() {
        return digitosFinales;
    }

    public void setDigitosFinales(String digitosFinales) {
        this.digitosFinales = digitosFinales;
    }

    public String getTokenSimulado() {
        return tokenSimulado;
    }

    public void setTokenSimulado(String tokenSimulado) {
        this.tokenSimulado = tokenSimulado;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}
