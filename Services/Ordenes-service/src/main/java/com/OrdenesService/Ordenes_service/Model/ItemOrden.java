package com.OrdenesService.Ordenes_service.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Embeddable
public class ItemOrden {
    @NotNull
    @Positive
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Positive
    @Column(nullable = false)
    private int cantidad;

    public ItemOrden() {
    }

    public ItemOrden(Long productoId, int cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}