package com.electroshop.model;

import com.electroshop.model.Producto;
import java.io.Serializable;

public class CartItem implements Serializable {

    // Campo 1: El objeto Producto que se está comprando
    private Producto producto;

    // Campo 2: La cantidad de ese producto
    private int cantidad;

    // --- CONSTRUCTOR ---
    // Constructor vacío (siempre buena práctica)
    public CartItem() {
    }

    // Constructor completo para facilitar la creación de ítems
    public CartItem(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    // --- GETTERS y SETTERS ---

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
}