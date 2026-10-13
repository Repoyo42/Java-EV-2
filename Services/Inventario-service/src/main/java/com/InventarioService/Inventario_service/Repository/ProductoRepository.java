package com.InventarioService.Inventario_service.Repository;

import com.InventarioService.Inventario_service.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {
    List<Producto> findBySku(String sku);

}
