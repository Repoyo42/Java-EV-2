package com.OrdenesService.Ordenes_service.Repository;

import com.OrdenesService.Ordenes_service.Model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByClienteId(Long clienteId);
}