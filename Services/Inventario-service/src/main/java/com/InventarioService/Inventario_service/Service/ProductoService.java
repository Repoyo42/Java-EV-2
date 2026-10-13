package com.InventarioService.Inventario_service.Service;

import com.InventarioService.Inventario_service.Model.Producto;
import com.InventarioService.Inventario_service.Repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    //Eliminar
    public boolean eliminar(Long id){
        if(repository.existsById(id)){
            repository.deleteById(id);
            return true;
        }
        return false;
    }
    //Buscar por id
    public Optional<Producto> buscaPorId (Long id){

        return repository.findById(id);
    }
    //Guardar
    public Producto guardar(Producto p){
        return repository.save(p);
    }

    //Obterner todos
    public List<Producto> obtenerTodos(){

        return repository.findAll();
    }


    //Actualizar
    public Optional<Producto> actualizar(Long id, Producto datos){
        return repository.findById(id)
                .map(producto -> {
                    producto.setNombre(datos.getNombre());
                    producto.setSku(datos.getSku());
                    producto.setDescripcion(datos.getDescripcion());
                    producto.setCantidad(datos.getCantidad());


                    return repository.save(producto);
                });
    }


}
