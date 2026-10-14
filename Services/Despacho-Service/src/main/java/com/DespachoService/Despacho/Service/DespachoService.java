package com.DespachoService.Despacho.Service;

import com.DespachoService.Despacho.Model.Despacho;
import com.DespachoService.Despacho.Model.EstadoDespacho;
import com.DespachoService.Despacho.Repository.DespachoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DespachoService {
    private final DespachoRepository repository;

    public DespachoService(DespachoRepository repository) {
        this.repository = repository;
    }

    public Despacho crear(Despacho despacho) {
        return repository.save(despacho);
    }

    public List<Despacho> listar() {
        return repository.findAll();
    }

    public Optional<Despacho> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<Despacho> buscarPorOrdenId(Long ordenId) {
        return repository.findByOrdenId(ordenId);
    }

    public boolean ordenTieneDespacho(Long ordenId) {
        return repository.existsByOrdenId(ordenId);
    }

    public Optional<Despacho> cambiarEstado(Long id, EstadoDespacho nuevoEstado) {
        return repository.findById(id)
                .filter(despacho -> transicionPermitida(despacho.getEstado(), nuevoEstado))
                .map(despacho -> {
                    despacho.setEstado(nuevoEstado);
                    return repository.save(despacho);
                });
    }

    public boolean transicionPermitida(EstadoDespacho actual, EstadoDespacho siguiente) {
        return switch (actual) {
            case PENDIENTE -> siguiente == EstadoDespacho.EN_PICKING || siguiente == EstadoDespacho.CANCELADO;
            case EN_PICKING -> siguiente == EstadoDespacho.PREPARADO || siguiente == EstadoDespacho.CANCELADO;
            case PREPARADO -> siguiente == EstadoDespacho.EN_TRANSITO || siguiente == EstadoDespacho.CANCELADO;
            case EN_TRANSITO -> siguiente == EstadoDespacho.ENTREGADO;
            case ENTREGADO, CANCELADO -> false;
        };
    }
}