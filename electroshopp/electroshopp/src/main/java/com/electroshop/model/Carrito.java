package com.electroshop.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carritos")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación Uno a Uno: Un Carrito pertenece a UN Usuario.
     * fetch = FetchType.LAZY: No cargues el usuario a menos que lo pida.
     * optional = false: Un carrito NO PUEDE existir sin un usuario.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Relación Uno a Muchos: Un Carrito tiene MUCHOS Items.
     * cascade = CascadeType.ALL: Si borro el carrito, se borran todos los items.
     * orphanRemoval = true: Si quito un item de esta lista, se borra de la BD.
     */
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoItem> items = new ArrayList<>();

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

    public List<CarritoItem> getItems() {
        return items;
    }

    public void setItems(List<CarritoItem> items) {
        this.items = items;
    }
}