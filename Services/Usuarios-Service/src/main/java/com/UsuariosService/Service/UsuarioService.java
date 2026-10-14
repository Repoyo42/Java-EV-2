package com.UsuariosService.Service;

import com.UsuariosService.Model.Usuario;
import com.UsuariosService.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario crear(Usuario usuario) {
        return repository.save(usuario);
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<Usuario> actualizar(Long id, Usuario datos) {
        return repository.findById(id)
                .map(usuario -> {
                    usuario.setNombre(datos.getNombre());
                    usuario.setEmail(datos.getEmail());
                    usuario.setRoles(datos.getRoles());
                    return repository.save(usuario);
                });
    }

    public boolean eliminar(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }

    public boolean emailEnUso(String email, Long idExcluido) {
        if (idExcluido == null) {
            return repository.existsByEmail(email);
        }

        return repository.existsByEmailAndIdNot(email, idExcluido);
    }
}