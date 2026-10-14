package com.OrdenesService.Ordenes_service.Controller;

import com.OrdenesService.Ordenes_service.Model.Orden;
import com.OrdenesService.Ordenes_service.Service.OrdenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
public class OrdenController {
    private final OrdenService service;

    public OrdenController(OrdenService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Orden> crear(@Valid @RequestBody Orden orden) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(orden));
    }

    @GetMapping
    public List<Orden> listar() {
        return service.listar();
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Orden> listarPorCliente(@PathVariable Long clienteId) {
        return service.listarPorCliente(clienteId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orden> actualizar(@PathVariable Long id, @Valid @RequestBody Orden datos) {
        return service.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!service.eliminar(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}