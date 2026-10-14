package com.OrdenesService.Ordenes_service.Service;

import com.OrdenesService.Ordenes_service.Model.Orden;
import com.OrdenesService.Ordenes_service.Repository.OrdenRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdenService {
    private final OrdenRepository repository;

    public OrdenService(OrdenRepository repository) {
        this.repository = repository;
    }

    public Orden crear(Orden orden) {
        return repository.save(orden);
    }

    public List<Orden> listar() {
        return repository.findAll();
    }

    public List<Orden> listarPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }

    public Optional<Orden> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<Orden> actualizar(Long id, Orden datos) {
        return repository.findById(id)
                .map(orden -> {
                    orden.setClienteId(datos.getClienteId());
                    orden.setItems(datos.getItems());
                    orden.setEstado(datos.getEstado());
                    return repository.save(orden);
                });
    }

    public boolean eliminar(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
}