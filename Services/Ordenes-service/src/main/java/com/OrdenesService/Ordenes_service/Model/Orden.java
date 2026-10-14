package com.OrdenesService.Ordenes_service.Model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes")
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Long clienteId;

    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "orden_items", joinColumns = @JoinColumn(name = "orden_id"))
    @OrderColumn(name = "posicion")
    private List<@jakarta.validation.Valid ItemOrden> items = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado = EstadoOrden.PENDIENTE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creadaEn;

    public Orden() {
    }

    public Orden(Long id, Long clienteId, List<ItemOrden> items, EstadoOrden estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.items = items;
        this.estado = estado == null ? EstadoOrden.PENDIENTE : estado;
    }

    @PrePersist
    void establecerFechaCreacion() {
        if (creadaEn == null) {
            creadaEn = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemOrden> getItems() {
        return items;
    }

    public void setItems(List<ItemOrden> items) {
        this.items = items;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public LocalDateTime getCreadaEn() {
        return creadaEn;
    }

    public void setCreadaEn(LocalDateTime creadaEn) {
        this.creadaEn = creadaEn;
    }
}