package com.DespachoService.Despacho.Controller;

import com.DespachoService.Despacho.Model.Despacho;
import com.DespachoService.Despacho.Model.EstadoDespacho;
import com.DespachoService.Despacho.Service.DespachoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/despachos")
public class DespachoController {
    private final DespachoService service;

    public DespachoController(DespachoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Despacho> crear(@Valid @RequestBody Despacho despacho) {
        if (service.ordenTieneDespacho(despacho.getOrdenId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(despacho));
    }

    @GetMapping
    public List<Despacho> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Despacho> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<Despacho> buscarPorOrdenId(@PathVariable Long ordenId) {
        return service.buscarPorOrdenId(ordenId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Despacho> cambiarEstado(
            @PathVariable Long id,
            @RequestBody EstadoDespacho nuevoEstado) {
        return service.cambiarEstado(id, nuevoEstado)
                .map(ResponseEntity::ok)
                .orElseGet(() -> service.buscarPorId(id).isPresent()
                        ? ResponseEntity.status(HttpStatus.CONFLICT).build()
                        : ResponseEntity.notFound().build());
    }
}